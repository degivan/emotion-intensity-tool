package ru.degtiarenko.dataart.storage;

public interface Storage<T> {
    Iterable<T> findAll();
    Iterable<String> findAllIds();
    T findById(String id);
    T create(T value);
    Iterable<T> create(Iterable<T> values);
}
