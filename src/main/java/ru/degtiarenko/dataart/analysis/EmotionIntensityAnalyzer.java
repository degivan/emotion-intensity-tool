package ru.degtiarenko.dataart.analysis;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import ru.degtiarenko.dataart.common.TextData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Client wraper for analyzer server.
 */
@Service
public class EmotionIntensityAnalyzer {

    private final String analyzerUrl;

    public EmotionIntensityAnalyzer(String analyzerUrl) {
        this.analyzerUrl = analyzerUrl;
    }

    public <T extends TextData> List<AnalysedData<T>> analyseData(List<T> tweets) throws IOException {
        List<Map<Emotion, Double>> intensities = countIntensity(tweets);

        List<AnalysedData<T>> result = new ArrayList<>();
        for (int i = 0; i < Math.max(intensities.size(), tweets.size()); i++) {
            T tweet = tweets.get(i);
            Map<Emotion, Double> intensity = intensities.get(i);
            result.add(new AnalysedData<>(intensity, tweet));
        }
        return result;
    }

    private <T extends TextData> List<Map<Emotion, Double>> countIntensity(List<T> tweets) throws IOException {
        JSONObject request = new JSONObject();
        JSONArray jsonTweets = new JSONArray();
        for(TextData tweet : tweets) {
            Map<String, String> jsonTweet = new HashMap<>();
            jsonTweet.put("text", tweet.getText());
            jsonTweets.put(jsonTweet);
        }
        request.put("tweets", jsonTweets);


        String jsonPredictions = Jsoup.connect(analyzerUrl)
                .requestBody(request.toString())
                .header("content-type", "application/json")
                .header("accept", "application/json")
                .ignoreContentType(true)
                .method(Connection.Method.POST)
                .execute()
                .body();

        return extractIntensities(jsonPredictions); //TODO
    }


    private List<Map<Emotion, Double>> extractIntensities(String jsonPredictions) {
        List<Map<Emotion, Double>> result = new ArrayList<>();
        JSONArray allPredictions = new JSONObject(jsonPredictions).getJSONArray("predictions");

        for (int i = 0; i < allPredictions.length(); i++) {
            Map<Emotion, Double> emotionIntensities = new HashMap<>();
            JSONObject predictions = allPredictions.getJSONObject(i);

            for (Emotion emotion : Emotion.values()) {
                Double intensity = predictions.getDouble(emotion.name().toLowerCase());
                emotionIntensities.put(emotion, intensity);
            }

            result.add(emotionIntensities);
        }

        return result;
    }
}
