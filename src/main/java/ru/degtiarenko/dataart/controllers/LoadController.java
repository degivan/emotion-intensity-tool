package ru.degtiarenko.dataart.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.degtiarenko.dataart.LoadTweetService;
import ru.degtiarenko.dataart.analysis.AnalysedData;
import ru.degtiarenko.dataart.twitter.Tweet;

import java.util.List;

@RestController
public class LoadController {
    @Autowired
    private LoadTweetService loadService;


    @GetMapping("/load")
    public List<AnalysedData<Tweet>> loadTweets(@RequestParam String hashTag) throws Exception {
        return loadService.loadAndAnalyse(hashTag);
    }
}
