package ru.degtiarenko.dataart.twitter;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.degtiarenko.dataart.analysis.AnalysedData;
import ru.degtiarenko.dataart.analysis.EmotionIntensityAnalyzer;
import ru.degtiarenko.dataart.storage.TweetStorage;

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
    public List<AnalysedData<Tweet>> loadAndAnalyse(String query, int lastHours) throws IOException, ParseException, UnirestException {
        DateTime since = new DateTime().minusHours(lastHours);
        List<Tweet> tweets = searchService.getTweetsWithQuery(query, since);
        List<AnalysedData<Tweet>> results = analyzer.analyseData(tweets);

        tweetStorage.create(results);
        return results;
    }
}
