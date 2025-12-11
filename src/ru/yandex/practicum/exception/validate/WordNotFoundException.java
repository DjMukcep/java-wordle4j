package ru.yandex.practicum.exception.validate;

import java.io.Serial;

public class WordNotFoundException extends Exception {

    @Serial
    private static final long serialVersionUID = 2204003125402437247L;

    public WordNotFoundException(String message) {
        super(message);
    }
}
