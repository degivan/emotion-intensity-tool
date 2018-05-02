package ru.degtiarenko.dataart;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.degtiarenko.dataart.analysis.AnalysedData;
import ru.degtiarenko.dataart.analysis.EmotionIntensityAnalyzer;
import ru.degtiarenko.dataart.storage.TweetStorage;
import ru.degtiarenko.dataart.twitter.Tweet;
import ru.degtiarenko.dataart.twitter.TweetSearchService;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Service
public class LoadTweetServiceImpl implements LoadTweetService {
    @Autowired
    private TweetSearchService searchService;
    @Autowired
    private EmotionIntensityAnalyzer analyzer;
    @Autowired
    private TweetStorage tweetStorage;

    public LoadTweetServiceImpl(TweetSearchService searchService, EmotionIntensityAnalyzer analyzer, TweetStorage tweetStorage) {
        this.searchService = searchService;
        this.analyzer = analyzer;
        this.tweetStorage = tweetStorage;
    }

    @Override
    public List<AnalysedData<Tweet>> loadAndAnalyse(String query) throws IOException, ParseException, UnirestException {
        DateTime since = new DateTime().minusHours(24);
        List<Tweet> tweets = searchService.getTweetsWithQuery(query, since);
        List<AnalysedData<Tweet>> results = analyzer.analyseData(tweets);

        tweetStorage.create(results);
        return results;
    }
}
