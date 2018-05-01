package ru.degtiarenko.dataart.twitter;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

import java.text.ParseException;
import java.util.List;

public class TweetWalker {
    private static final String HASHTAG_PREFIX = "%23";
    private final String query;
    private final String authValue;
    private final String searchUrl;
    private String lastId = "";
    private final TweetParser parser;

    public TweetWalker(String query, String authValue, String searchUrl) {
        this.query = query;
        this.authValue = authValue;
        this.searchUrl = searchUrl;
        this.parser = new TweetParser();
    }

    public List<Tweet> getNextTweets() throws UnirestException, ParseException {
        HttpRequest base = Unirest.get(searchUrl)
                .queryString("q", query)
                .queryString("result_type", "recent")
                .queryString("count", "100")
                .queryString("lang","en")
                .header("Authorization", authValue);
        if (!lastId.equals("")) {
            base = base.queryString("max_id", lastId);
        }
        List<Tweet> tweets = parser.getTweetsFromResponse(base.asJson());
        if (!tweets.isEmpty()) {
            lastId = tweets.get(tweets.size() - 1).getId();
        }
        return tweets;
    }
}
