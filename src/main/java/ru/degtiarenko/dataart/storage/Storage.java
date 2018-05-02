package ru.degtiarenko.dataart.storage;

import org.springframework.stereotype.Component;

@Component
public interface Storage<T> {
    Iterable<T> findAll();
    Iterable<String> findAllIds();
    T findById(String id);
    T create(T value);
    Iterable<T> create(Iterable<T> values);
}
