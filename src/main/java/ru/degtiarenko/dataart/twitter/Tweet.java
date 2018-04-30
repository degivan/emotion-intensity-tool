package ru.degtiarenko.dataart.twitter;


import org.joda.time.DateTime;

public class Tweet {
    private final DateTime date;
    private final String id;
    private final String text;

    public Tweet(DateTime date, String id, String text) {
        this.date = date;
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public DateTime getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tweet tweet = (Tweet) o;

        return id != null ? id.equals(tweet.id) : tweet.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
