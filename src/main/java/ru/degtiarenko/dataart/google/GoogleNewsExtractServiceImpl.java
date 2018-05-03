package ru.degtiarenko.dataart.google;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GoogleNewsExtractServiceImpl implements GoogleNewsExtractService {
    private static final String QUERY_FORMAT =
            "https://news.google.com/news/rss/search/section/q/%s/%s?hl=en&gl=US&ned=us";

    @Override
    public List<GoogleHeader> getNewsWithQuery(String query) throws IOException {
        query = query.toLowerCase();
        String url = String.format(QUERY_FORMAT, query, query);
        Document result = Jsoup.connect(url)
                .ignoreContentType(true)
                .get();
        List<GoogleHeader> headers = extractHeaders(result.toString());
        return headers;
    }

    private List<GoogleHeader> extractHeaders(String xml) {
        List<GoogleHeader> headers = new ArrayList<>();
        Pattern r = Pattern.compile("<title>[\\s\\S]*?</title>");
        Matcher m = r.matcher(xml);

        List<String> matches = new ArrayList<>();
        while (m.find()) {
            matches.add(m.group());
        }
        for (int i = 2; i < matches.size(); i++) {
            String titledHeader = matches.get(i);
            String header = titledHeader.substring(7, titledHeader.length() - 8);
            headers.add(new GoogleHeader(header));
        }
        return headers;
    }
}
