package ru.degtiarenko.dataart.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.degtiarenko.dataart.LoadTweetService;
import ru.degtiarenko.dataart.analysis.AnalysedData;
import ru.degtiarenko.dataart.twitter.Tweet;

import java.util.List;

@Controller
public class LoadController {
    @Autowired
    private LoadTweetService loadService;

    public LoadController(LoadTweetService loadService) {
        this.loadService = loadService;
    }

    @GetMapping("/load")
    public List<AnalysedData<Tweet>> loadTweets(@RequestParam String hashTag) throws Exception {
        return loadService.loadAndAnalyse(hashTag);
    }
}
