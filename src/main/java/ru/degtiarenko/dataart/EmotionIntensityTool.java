package ru.degtiarenko.dataart;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.joda.time.DateTime;
import ru.degtiarenko.dataart.analysis.Emotion;
import ru.degtiarenko.dataart.analysis.EmotionIntensityAnalyzer;
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
    private static final String DATA_ART = "DataArt";
    private static final String PATH_TO_CONFIG = "config.properties";
    private static final String ANALYZER_URL_PROPERTY = "analyzer_url";
    private static final String BEARER_TOKEN_PROPERTY = "bearer_token";
    private static final String SEARCH_URL_PROPERTY = "search_url";

    public static void main(String[] args) throws IOException, ParseException, UnirestException {
        Properties configuration = new Properties();
        InputStream input = ClassLoader.getSystemResourceAsStream(PATH_TO_CONFIG);
        configuration.load(input);
        input.close();

        String analyzerUrl = configuration.getProperty(ANALYZER_URL_PROPERTY);
        String bearerToken = configuration.getProperty(BEARER_TOKEN_PROPERTY);
        String searchUrl = configuration.getProperty(SEARCH_URL_PROPERTY);

        EmotionIntensityAnalyzer analyzer = new EmotionIntensityAnalyzer(analyzerUrl);
        SearchService tweetSearchService = new SearchServiceImpl(bearerToken, searchUrl);

        DateTime hourAgo = new DateTime().minusHours(1);
        List<Tweet> tweets = tweetSearchService.getTweetsWithHashTag(DATA_ART, hourAgo);
        List<Map<Emotion, Double>> results = analyzer.predictIntensity(tweets);

        System.out.println(results);
    }
}
