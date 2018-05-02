package ru.degtiarenko.dataart;

import ru.degtiarenko.dataart.analysis.AnalysedData;
import ru.degtiarenko.dataart.twitter.Tweet;

import java.util.List;


public interface LoadTweetService {
    List<AnalysedData<Tweet>> loadAndAnalyse(String hashTag) throws Exception;
}
