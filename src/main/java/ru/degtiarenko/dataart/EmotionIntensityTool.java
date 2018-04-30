package ru.degtiarenko.dataart;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.joda.time.DateTime;
import ru.degtiarenko.dataart.twitter.SearchService;
import ru.degtiarenko.dataart.twitter.SearchServiceImpl;
import ru.degtiarenko.dataart.twitter.Tweet;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Starting point.
 */
public class EmotionIntensityTool {
    public static final String DATA_ART = "DataArt";
    private static final String PATH_TO_CONFIG = "config.properties";

    public static void main(String[] args) throws IOException, ParseException, UnirestException {
        Properties prop = new Properties();
        InputStream input = ClassLoader.getSystemResourceAsStream(PATH_TO_CONFIG);
        prop.load(input);
        input.close();

        String analyzerUrl = prop.getProperty("analyzer_url");
        String bearerToken = prop.getProperty("bearer_token");
        String searchUrl = prop.getProperty("search_url");

        EmotionIntensityAnalyzer analyzer = new EmotionIntensityAnalyzer(analyzerUrl);
        SearchService tweetSearchService = new SearchServiceImpl(bearerToken, searchUrl);

        DateTime hourAgo = new DateTime().minusHours(1);
        List<Tweet> tweets = tweetSearchService.getTweetsWithHashTag(DATA_ART, hourAgo);
        List<Map<Emotion, Double>> results = analyzer.predictIntensity(tweets);

        System.out.println(results);
    }
}
