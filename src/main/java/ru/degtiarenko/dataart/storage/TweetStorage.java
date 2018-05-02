package ru.degtiarenko.dataart.storage;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.util.maven.example.tables.Tweets;
import org.jooq.util.maven.example.tables.records.TweetsRecord;
import org.springframework.stereotype.Component;
import ru.degtiarenko.dataart.analysis.AnalysedData;
import ru.degtiarenko.dataart.analysis.TweetToRecordConverter;
import ru.degtiarenko.dataart.twitter.Tweet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TweetStorage implements Storage<AnalysedData<Tweet>> {
    private Connection conn = DriverManager.getConnection("jdbc:postgresql:eitool", "eitool", "eitool");
    private DSLContext dslContext = DSL.using(conn, SQLDialect.POSTGRES);

    public TweetStorage() throws SQLException {
    }

    @Override
    public List<String> findAllIds() {
        return dslContext.select(Tweets.TWEETS.ID)
                .from(Tweets.TWEETS)
                .fetch()
                .into(String.class);
    }

    @Override
    public Iterable<AnalysedData<Tweet>> findAll() {
        return dslContext.select()
                .from(Tweets.TWEETS)
                .fetch()
                .into(TweetsRecord.class)
                .stream()
                .map(TweetToRecordConverter::from)
                .collect(Collectors.toList());
    }

    @Override
    public AnalysedData<Tweet> findById(String id) {
        List<TweetsRecord> result = dslContext.select()
                .from(Tweets.TWEETS)
                .where(Tweets.TWEETS.ID.eq(id)).fetch()
                .into(TweetsRecord.class);
        if (result.isEmpty()) {
            return null;
        } else {
            return TweetToRecordConverter.from(result.get(0));
        }
    }

    @Override
    public AnalysedData<Tweet> create(AnalysedData<Tweet> value) {
        dslContext.insertInto(Tweets.TWEETS)
                .set(TweetToRecordConverter.toRecord(value))
                .execute();

        return value;
    }

    @Override
    public Iterable<AnalysedData<Tweet>> create(Iterable<AnalysedData<Tweet>> values) {
        List<String> ids = findAllIds();

        for (AnalysedData<Tweet> value : values) {
            if(!ids.contains(value.getData().getId())) {
                create(value);
            }
        }

        return values;
    }
}
