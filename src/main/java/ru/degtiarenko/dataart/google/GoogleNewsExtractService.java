package ru.degtiarenko.dataart.google;

import java.io.IOException;
import java.util.List;

public interface GoogleNewsExtractService {
    List<GoogleHeader> getNewsWithQuery(String query) throws IOException;
}
