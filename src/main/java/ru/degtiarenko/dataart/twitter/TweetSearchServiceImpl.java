package ru.degtiarenko.dataart.twitter;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TweetSearchServiceImpl implements TweetSearchService {
    private static final String AUTH_PREFIX = "Bearer ";
    private final String authValue;
    private final String searchUrl;

    public TweetSearchServiceImpl(String bearerToken, String searchUrl) {
        this.authValue = AUTH_PREFIX + bearerToken;
        this.searchUrl = searchUrl;
    }

    @Override
    public List<Tweet> getTweetsWithQuery(String query, DateTime since) throws ParseException, UnirestException {
        DateTime lastTweetTime = new DateTime();
        TweetWalker tweetWalker = new TweetWalker(query, authValue, searchUrl);
        List<Tweet> tweets = new ArrayList<>();
        while (lastTweetTime.isAfter(since)) {
            List<Tweet> newTweets = tweetWalker.getNextTweets();
            if (newTweets.isEmpty()) {
                break;
            }
            lastTweetTime = newTweets.get(newTweets.size() - 1).getDate();
            tweets.addAll(newTweets);
        }
        tweets = tweets.stream()
                .distinct()
                .collect(Collectors.toList());
        return tweets;
    }
}
