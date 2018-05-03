package ru.degtiarenko.dataart.twitter;

import ru.degtiarenko.dataart.analysis.AnalysedData;

import java.util.List;


public interface LoadTweetService {
    List<AnalysedData<Tweet>> loadAndAnalyse(String hashTag, int lastHours) throws Exception;
}
