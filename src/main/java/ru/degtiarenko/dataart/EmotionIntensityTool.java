package ru.degtiarenko.dataart;

import java.io.IOException;
import java.util.Map;

/**
 * Starting point.
 */
public class EmotionIntensityTool {
    public static void main(String[] args) throws IOException {
        final String analyzerUrl = "http://0.0.0.0:8000/predict";
        EmotionIntensityAnalyzer analyzer = new EmotionIntensityAnalyzer(analyzerUrl);

        Map<Emotion, Double> results = analyzer.predictIntensity(new Tweet("Im SOOOOO ANGRY about my diploma"));
        System.out.println(results);
    }
}
