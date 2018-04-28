import flask
import numpy as np
from keras import backend as K
from keras.models import load_model

app = flask.Flask('analyzer')
model = None


def pearson_correlation_f(y_true, y_pred):
    fsp = y_pred - K.mean(y_pred, axis=-1, keepdims=True)
    fst = y_true - K.mean(y_true, axis=-1, keepdims=True)

    devP = K.std(y_pred)
    devT = K.std(y_true)

    val = K.mean(fsp * fst) / (devP * devT)

    return 1 - val


def load():  # TODO
    global vectorizer
    global doc2vec_model
    global model
    K.clear_session()
    model = load_model('/home/vanyadeg/dev/diploma/tweet_emotions/nn_0.6396_joy.h5',
                       custom_objects={'pearson_correlation_f': pearson_correlation_f})
    model._make_predict_function()
    print(model.summary())

# TODO
def extract_features(text):
    return np.zeros((1, 3791))


@app.route("/predict", methods=["GET"])
def predict():
    data = {"success": False}
    text = flask.request.args.get('tweet', '')
    preds = model.predict(extract_features(text))
    print(preds)
    data["predictions"] = [preds.tolist()[0][2]]
    data["success"] = True
    print(data)
    return flask.jsonify(data)


load()

if __name__ == "__main__":
    app.run()
