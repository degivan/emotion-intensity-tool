package ru.degtiarenko.dataart.twitter;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.util.List;

public interface SearchService {
    List<Tweet> getTweetsWithQuery(String query, DateTime since) throws ParseException, UnirestException;
}
