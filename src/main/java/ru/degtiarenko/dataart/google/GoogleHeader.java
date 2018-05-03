package ru.degtiarenko.dataart.google;

import ru.degtiarenko.dataart.common.TextData;

public class GoogleHeader implements TextData {
    private final String text;

    public GoogleHeader(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }
}
