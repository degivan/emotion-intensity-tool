package ru.degtiarenko.dataart.storage;

import ru.degtiarenko.dataart.analysis.AnalysedTweet;

public interface Storage<T> {
    Iterable<T> findAll();
    T findById(String id);
    T create(T value);
    Iterable<AnalysedTweet> create(Iterable<T> values);
}
