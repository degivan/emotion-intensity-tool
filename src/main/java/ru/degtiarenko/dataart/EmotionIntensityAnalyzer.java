package ru.degtiarenko.dataart;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Client wraper for analyzer server.
 */
public class EmotionIntensityAnalyzer {

    private final String analyzerUrl;

    public EmotionIntensityAnalyzer(String analyzerUrl) {
        this.analyzerUrl = analyzerUrl;
    }

    public Map<Emotion, Double> predictIntensity(Tweet tweet) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("tweet", tweet.getText());

        String json = Jsoup.connect(analyzerUrl)
                .data(parameters)
                .ignoreContentType(true)
                .get()
                .body()
                .text(); //TODO: change jsoup for json-something

        return extractIntensities(json);
    }

    //TODO
    private Map<Emotion, Double> extractIntensities(String jsonString) {
        final Map<Emotion, Double> emotionIntensities = new HashMap<>();

        JSONObject jsonObj = new JSONObject(jsonString);
        JSONArray predictions = jsonObj.getJSONArray("predictions");

        emotionIntensities.put(Emotion.JOY, predictions.getDouble(0));

        return emotionIntensities;
    }
}
