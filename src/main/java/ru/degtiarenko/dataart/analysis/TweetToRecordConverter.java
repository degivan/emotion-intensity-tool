package ru.degtiarenko.dataart.analysis;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.jooq.util.maven.example.tables.records.TweetsRecord;
import org.json.JSONObject;
import ru.degtiarenko.dataart.twitter.Tweet;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class TweetToRecordConverter {
    private static final double DEFAULT_INTENSITY = 0.0;

    private TweetToRecordConverter() {}


    public static AnalysedData<Tweet> from(TweetsRecord tweetsRecord) {
        String jsonIntensities = tweetsRecord.getIntensities();
        Date postedAt = tweetsRecord.getPostedAt();
        String text = tweetsRecord.getText();
        String recordId = tweetsRecord.getId();

        DateTime date = LocalDate.fromDateFields(postedAt)
                .toDateTimeAtCurrentTime();
        Tweet tweet = new Tweet(date, recordId, text);
        Map<Emotion, Double> emotionIntensities = extractIntensitiesFromJson(jsonIntensities);

        return new AnalysedData<>(emotionIntensities, tweet);
    }

    private static Map<Emotion, Double> extractIntensitiesFromJson(String jsonIntensities) {
        final Map<Emotion, Double> emotionIntensities = new HashMap<>();

        JSONObject jsonObj = new JSONObject(jsonIntensities);
        for (Emotion emotion : Emotion.values()) {
            Double intensity = jsonObj.optDouble(emotion.name(), DEFAULT_INTENSITY);
            emotionIntensities.put(emotion, intensity);
        }
        return emotionIntensities;
    }

    public static TweetsRecord toRecord(AnalysedData<Tweet> tweet) {
        TweetsRecord record = new TweetsRecord();
        String id = tweet.getData().getId();
        String text = tweet.getData().getText();
        DateTime date = tweet.getData().getDate();
        Map<Emotion, Double> intensities = tweet.getEmotionIntensities();

        record.setId(id);
        record.setPostedAt(new Date(date.getMillis()));
        record.setIntensities(convertIntensitiesToJson(intensities));
        record.setText(text);

        return record;
    }

    private static String convertIntensitiesToJson(Map<Emotion, Double> intensities) {
        JSONObject jsonObject = new JSONObject();

        for (Emotion emotion : intensities.keySet()) {
            jsonObject.put(emotion.name(), intensities.get(emotion));
        }

        return jsonObject.toString();
    }
}
