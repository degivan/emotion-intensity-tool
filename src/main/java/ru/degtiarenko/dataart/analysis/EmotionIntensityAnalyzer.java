package ru.degtiarenko.dataart.analysis;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import ru.degtiarenko.dataart.twitter.Tweet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Client wraper for analyzer server.
 */
public class EmotionIntensityAnalyzer {

    private final String analyzerUrl;

    public EmotionIntensityAnalyzer(String analyzerUrl) {
        this.analyzerUrl = analyzerUrl;
    }

    public List<AnalysedTweet> analyseTweets(List<Tweet> tweets) throws IOException {
        List<Map<Emotion, Double>> intensities = countIntensity(tweets);

        List<AnalysedTweet> result = new ArrayList<>();
        for (int i = 0; i < Math.max(intensities.size(), tweets.size()); i++) {
            Tweet tweet = tweets.get(i);
            Map<Emotion, Double> intensity = intensities.get(i);
            result.add(new AnalysedTweet(intensity, tweet));
        }
        return result;
    }

    private List<Map<Emotion, Double>> countIntensity(List<Tweet> tweets) throws IOException {
        JSONObject request = new JSONObject();
        JSONArray jsonTweets = new JSONArray();
        for(Tweet tweet : tweets) {
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
