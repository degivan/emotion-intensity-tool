package ru.degtiarenko.dataart.google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.degtiarenko.dataart.analysis.AnalysedData;
import ru.degtiarenko.dataart.analysis.EmotionIntensityAnalyzer;

import java.io.IOException;
import java.util.List;

@Service
public class LoadGoogleServiceImpl implements LoadGoogleService {
    @Autowired
    private GoogleNewsExtractService googleNewsExtractService;
    @Autowired
    private EmotionIntensityAnalyzer intensityAnalyzer;

    @Override
    public List<AnalysedData<GoogleHeader>> loadAndAnalyse(String query) throws IOException {
        List<GoogleHeader> headers = googleNewsExtractService.getNewsWithQuery(query);
        List<AnalysedData<GoogleHeader>> analysedHeaders = intensityAnalyzer.analyseData(headers);

        return analysedHeaders;
    }
}
