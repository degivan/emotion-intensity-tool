package ru.degtiarenko.dataart.twitter;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public interface TweetSearchService {
    List<Tweet> getTweetsWithQuery(String query, DateTime since) throws ParseException, UnirestException;
}
