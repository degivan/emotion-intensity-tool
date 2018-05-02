import flask
import numpy as np
from keras import backend as K
from keras.models import load_model
from keras.preprocessing import sequence
from keras.preprocessing.text import Tokenizer
import pickle

app = flask.Flask('analyzer')
models = {}
tokenizer = None
max_sent_len = 50


def pearson_correlation_f(y_true, y_pred):
    fsp = y_pred - K.mean(y_pred, axis=-1, keepdims=True)
    fst = y_true - K.mean(y_true, axis=-1, keepdims=True)

    devP = K.std(y_pred)
    devT = K.std(y_true)

    val = K.mean(fsp * fst) / (devP * devT)

    return 1 - val


def load():  # TODO
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


# TODO
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


load()

if __name__ == "__main__":
    app.run()
