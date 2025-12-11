package ru.yandex.practicum.exception.validate;

import java.io.Serial;

public class WrongLanguageException extends Exception {
    @Serial
    private static final long serialVersionUID = 7711895460584463699L;

    public WrongLanguageException(String message) {
        super(message);
    }
}
