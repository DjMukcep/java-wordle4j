package ru.yandex.practicum.exception.system;

import java.io.FileNotFoundException;
import java.io.Serial;

public class FileNotFound extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 4552095460088097076L;

    public FileNotFound(FileNotFoundException message) {
        super(message);
    }
}
