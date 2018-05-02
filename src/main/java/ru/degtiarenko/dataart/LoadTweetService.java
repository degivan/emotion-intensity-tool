package ru.degtiarenko.dataart;

import org.apache.felix.scr.annotations.Service;
import ru.degtiarenko.dataart.analysis.AnalysedData;
import ru.degtiarenko.dataart.twitter.Tweet;

import java.util.List;

@Service
public interface LoadTweetService {

    List<AnalysedData<Tweet>> loadAndAnalyse(String hashTag) throws Exception;
}
