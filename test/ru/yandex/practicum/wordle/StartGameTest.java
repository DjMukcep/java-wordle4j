package ru.yandex.practicum.wordle;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.Wordle;
import ru.yandex.practicum.exception.validate.BadWordLengthException;
import ru.yandex.practicum.exception.validate.WordNotFoundException;
import ru.yandex.practicum.exception.validate.WrongLanguageException;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
public class StartGameTest {

    private static PrintWriter sysWriter;
    private static PrintWriter valWriter;

    @BeforeAll
    static void setPrintWriters() {
        sysWriter = new PrintWriter(System.out);
        valWriter = new PrintWriter(System.out);
    }

    @BeforeEach
    void muteConsole() {
        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
    }

    @Test
    void shouldThrowWrongLanguageException() {
        InputStream input = new ByteArrayInputStream("jj\n".getBytes());
        Wordle wordle = new Wordle(sysWriter,valWriter,input);
        assertThrows(WrongLanguageException.class, wordle::play);
    }

    @Test
    void shouldThrowBadWordLengthException() {
        InputStream input = new ByteArrayInputStream("программа\n".getBytes());
        Wordle wordle = new Wordle(sysWriter,valWriter,input);
        assertThrows(BadWordLengthException.class, wordle::play);
    }

    @Test
    void shouldThrowWordNotFoundException() {
        InputStream input = new ByteArrayInputStream("транг\n".getBytes());
        Wordle wordle = new Wordle(sysWriter,valWriter,input);
        assertThrows(WordNotFoundException.class, wordle::play);
    }

    @Test
    void shouldProcessEmptyInput() throws Exception {
        InputStream input = new ByteArrayInputStream("\n".getBytes());
        Wordle wordle = new Wordle(sysWriter,valWriter,input);
        wordle.play();
        String word = wordle.getGame().getWord();
        String clue = wordle.getGame().getClue();

        assertNotNull(word);
        assertNotNull(clue);
    }

    @Test
    void shouldProcessValidInput() throws Exception {
        InputStream input = new ByteArrayInputStream("мечта\n".getBytes());
        Wordle wordle = new Wordle(sysWriter,valWriter,input);
        wordle.play();
        String word = wordle.getGame().getWord();
        String clue = wordle.getGame().getClue();
        assertNotNull(word);
        assertNotNull(clue);
        assertEquals("мечта",word);
    }
}
