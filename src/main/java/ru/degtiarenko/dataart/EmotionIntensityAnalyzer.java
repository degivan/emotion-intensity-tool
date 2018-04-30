package ru.degtiarenko.dataart;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import ru.degtiarenko.dataart.twitter.Tweet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Client wraper for analyzer server.
 */
public class EmotionIntensityAnalyzer {

    private final String analyzerUrl;

    public EmotionIntensityAnalyzer(String analyzerUrl) {
        this.analyzerUrl = analyzerUrl;
    }

    public List<Map<Emotion, Double>> predictIntensity(List<Tweet> tweets) {
        return tweets.stream()
                .map(tweet -> {
                    try {
                        return predictIntensity(tweet);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).collect(Collectors.toList());
    }

    public Map<Emotion, Double> predictIntensity(Tweet tweet) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("tweet", tweet.getText());

        String json = Jsoup.connect(analyzerUrl)
                .data(parameters)
                .ignoreContentType(true)
                .get()
                .body()
                .text();

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
