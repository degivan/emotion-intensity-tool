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
tokenizer = None
max_sent_len = 50


def load():
    global tokenizer
    global models
    K.clear_session()
    with open('IMS-EmoInt/keras_regression/tokenizer.pickle', 'rb') as handle:
        tokenizer = pickle.load(handle)
    for i in 'anger joy sadness fear'.split():
        model = load_model('IMS-EmoInt/keras_regression/models/%s.h5' % str(i))
        model._make_predict_function()
        models[str(i)] = model
    print('EMOTIONS:')
    print(models.keys())


def extract_features(text):
    text_sequence = sequence.pad_sequences(tokenizer.texts_to_sequences([text]), maxlen=max_sent_len)
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
            res = models[emotion].predict(extract_features(text))
            preds[emotion] = res[0][0].item()
        predictions.append(preds)
    data["predictions"] = predictions
    data["success"] = True
    return flask.jsonify(data)


@app.route("/train", methods=["POST"])
def train():
    data = {"success": False}
    json = flask.request.get_json()
    text = json['text']
    seq = extract_features(text)
    for emotion in models.keys():
        label = float(json[emotion])
        models[emotion].fit(seq, [label])
    data["success"] = True
    return flask.jsonify(data)


def shutdown():
    for i in 'anger joy sadness fear'.split():
        models[str(i)].save('IMS-EmoInt/keras_regression/models/%s.h5' % str(i))


load()
atexit.register(shutdown)

if __name__ == "__main__":
    app.run()
