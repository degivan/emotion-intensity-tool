package ru.degtiarenko.dataart.analysis;

import ru.degtiarenko.dataart.common.TextData;

import java.util.Map;

public abstract class AnalysedData<T extends TextData> {
    protected final Map<Emotion, Double> emotionIntensities;
    protected final T textData;

    protected AnalysedData(Map<Emotion, Double> emotionIntensities, T textData) {
        this.emotionIntensities = emotionIntensities;
        this.textData = textData;
    }

    public Map<Emotion, Double> getEmotionIntensities() {
        return emotionIntensities;
    }

    public T getData() {
        return textData;
    }
}