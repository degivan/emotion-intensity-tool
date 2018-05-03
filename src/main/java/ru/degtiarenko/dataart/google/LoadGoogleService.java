package ru.degtiarenko.dataart.google;

import ru.degtiarenko.dataart.analysis.AnalysedData;

import java.io.IOException;
import java.util.List;

public interface LoadGoogleService {
    List<AnalysedData<GoogleHeader>> loadAndAnalyse(String query) throws IOException;
}
