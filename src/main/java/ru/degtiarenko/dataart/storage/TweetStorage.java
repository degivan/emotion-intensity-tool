package ru.degtiarenko.dataart.storage;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.util.maven.example.tables.Tweets;
import org.jooq.util.maven.example.tables.records.TweetsRecord;
import ru.degtiarenko.dataart.analysis.AnalysedTweet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class TweetStorage implements Storage<AnalysedTweet> {
    private Connection conn = DriverManager.getConnection("jdbc:postgresql:eitool", "eitool", "eitool");
    private DSLContext dslContext = DSL.using(conn, SQLDialect.POSTGRES);

    public TweetStorage() throws SQLException {
    }

    @Override
    public Iterable<AnalysedTweet> findAll() {
        return dslContext.select()
                .from(Tweets.TWEETS)
                .fetch()
                .into(TweetsRecord.class)
                .stream()
                .map(AnalysedTweet::from)
                .collect(Collectors.toList());
    }

    @Override
    public AnalysedTweet findById(String id) {
        List<TweetsRecord> result = dslContext.select()
                .from(Tweets.TWEETS)
                .where(Tweets.TWEETS.ID.eq(id)).fetch()
                .into(TweetsRecord.class);
        if (result.isEmpty()) {
            return null;
        } else {
            return AnalysedTweet.from(result.get(0));
        }
    }

    @Override
    public AnalysedTweet create(AnalysedTweet value) {
        dslContext.insertInto(Tweets.TWEETS)
                .set(AnalysedTweet.toRecord(value))
                .execute();

        return value;
    }

    @Override
    public Iterable<AnalysedTweet> create(Iterable<AnalysedTweet> values) {
        for (AnalysedTweet value : values) {
            create(value);
        }

        return values;
    }
}
