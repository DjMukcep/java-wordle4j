package ru.yandex.practicum.exception.validate;

import java.io.Serial;

public class BadWordLengthException extends Exception {
  @Serial
  private static final long serialVersionUID = -7913822787618977178L;

  public BadWordLengthException(String message) {
        super(message);
    }
}
