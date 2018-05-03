import flask
import numpy as np
from keras import backend as K
from keras.models import load_model
from keras.preprocessing import sequence
from keras.preprocessing.text import Tokenizer
import pickle
import atexit

app = flask.Flask('analyzer')
models = {}
tokenizers = {}
max_sent_len = 36
cl_from_emotion = {'anger': 0, 'sadness': 1, 'joy': 2, 'fear': 3}
emotion_coeff = {'anger': 0.3, 'sadness': 0.3, 'joy': 1.5, 'fear': 0.1}
position_from_emotion = {'anger': [1, 0, 0, 0], 'sadness': [0, 1, 0, 0], 'joy': [0, 0, 1, 0], 'fear': [0, 0, 0, 1]}


def load():
    global tokenizers
    global models
    K.clear_session()
    for i in 'anger joy sadness fear'.split():
        with open('networks/tokenizer_%s.pickle' % str(i), 'rb') as handle:
            tokenizers[str(i)] = pickle.load(handle)
        model = load_model('networks/%s.h5' % str(i))
        model._make_predict_function()
        models[str(i)] = model
    print('EMOTIONS:')
    print(models.keys())


def extract_features(text, emotion):
    text_sequence = sequence.pad_sequences(tokenizers[emotion].texts_to_sequences([text]), maxlen=36)
    return text_sequence


@app.route("/predict", methods=["POST"])
def predict():
    data = {"success": False}
    json = flask.request.get_json()
    predictions = []
    for tweet in json['tweets']:
        text = tweet['text']
        preds = {}
        for emotion in models.keys():
            res = models[emotion].predict(extract_features(text, emotion))
            extracted_res = res[0][cl_from_emotion[emotion]].item() * emotion_coeff[emotion]
            preds[emotion] = extracted_res
        predictions.append(preds)
    data["predictions"] = predictions
    data["success"] = True
    return flask.jsonify(data)


@app.route("/train", methods=["POST"])
def train():
    data = {"success": False}
    json = flask.request.get_json()
    text = json['text']
    for emotion in models.keys():
        seq = extract_features(text, emotion)
        val = float(json[emotion])
        label = [val * x for x in position_from_emotion[emotion]]
        models[emotion].fit(seq, np.array([label]))
    data["success"] = True
    return flask.jsonify(data)


def shutdown():
    for i in 'anger joy sadness fear'.split():
        models[str(i)].save('networks/%s.h5' % str(i))


load()
atexit.register(shutdown)

if __name__ == "__main__":
    app.run()
