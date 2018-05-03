package ru.degtiarenko.dataart.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.degtiarenko.dataart.twitter.LoadTweetService;
import ru.degtiarenko.dataart.analysis.AnalysedData;
import ru.degtiarenko.dataart.google.GoogleHeader;
import ru.degtiarenko.dataart.google.LoadGoogleService;
import ru.degtiarenko.dataart.twitter.Tweet;

import java.util.List;

@RestController
public class LoadController {
    @Autowired
    private LoadTweetService loadTweetService;
    @Autowired
    private LoadGoogleService loadGoogleService;


    @GetMapping("/load_tweets")
    public List<AnalysedData<Tweet>> loadTweets(@RequestParam String query) throws Exception {
        return loadTweetService.loadAndAnalyse(query);
    }

    @GetMapping("/load_news")
    public List<AnalysedData<GoogleHeader>> loadNews(@RequestParam String query) throws Exception {
        return loadGoogleService.loadAndAnalyse(query);
    }
}
