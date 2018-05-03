package ru.degtiarenko.dataart.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.degtiarenko.dataart.analysis.Emotion;
import ru.degtiarenko.dataart.twitter.LoadTweetService;
import ru.degtiarenko.dataart.analysis.AnalysedData;
import ru.degtiarenko.dataart.google.GoogleHeader;
import ru.degtiarenko.dataart.google.LoadGoogleService;
import ru.degtiarenko.dataart.twitter.Tweet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static ru.degtiarenko.dataart.analysis.Emotion.*;

@RestController
public class LoadController {
    @Autowired
    private LoadTweetService loadTweetService;
    @Autowired
    private LoadGoogleService loadGoogleService;


    @GetMapping("/load_tweets")
    public List<AnalysedData<Tweet>> loadTweets(@RequestParam String query, int lastHours) throws Exception {
        List<AnalysedData<Tweet>> result = loadTweetService.loadAndAnalyse(query, lastHours);
        return extractMinMax(result);
    }

    private List<AnalysedData<Tweet>> extractMinMax(List<AnalysedData<Tweet>> result) {
        List<AnalysedData<Tweet>> minAndMax = new ArrayList<>();
        Comparator<AnalysedData<Tweet>> joyComparator = Comparator.comparingDouble(at -> at.getEmotionIntensities().get(JOY));
        minAndMax.add(
                result.stream()
                        .min(joyComparator)
                        .get()
        );
        minAndMax.add(
                result.stream()
                        .max(joyComparator)
                        .get()
        );
        for (AnalysedData<Tweet> tweet : minAndMax) {
            tweet.getEmotionIntensities().remove(ANGER);
            tweet.getEmotionIntensities().remove(SADNESS);
            tweet.getEmotionIntensities().remove(FEAR);
        }
        return minAndMax;
    }

    @GetMapping("/load_news")
    public List<AnalysedData<GoogleHeader>> loadNews(@RequestParam String query) throws Exception {
        return loadGoogleService.loadAndAnalyse(query);
    }
}
