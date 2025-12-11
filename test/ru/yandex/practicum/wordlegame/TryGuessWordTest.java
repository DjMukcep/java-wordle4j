package ru.yandex.practicum.wordlegame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.WordleDictionaryLoader;
import ru.yandex.practicum.WordleGame;
import ru.yandex.practicum.exception.validate.BadWordLengthException;
import ru.yandex.practicum.exception.validate.WordNotFoundException;
import ru.yandex.practicum.exception.validate.WrongLanguageException;
import static org.junit.jupiter.api.Assertions.*;
import java.io.PrintWriter;

public class TryGuessWordTest {

    private WordleGame game;

    @BeforeEach
    void setUp() {
        var printWriter = new PrintWriter(System.out, true);
        var loader = new WordleDictionaryLoader(new PrintWriter(System.out,true));
        game = new WordleGame(loader, printWriter);
    }

    @Test
    void shouldThrowWrongLanguageExceptionWhenWordContainsNotRussianSymbols() {
        String word = "абгgsв";
        assertThrows(WrongLanguageException.class, () -> game.tryToGuessWord(word));
    }

    @Test
    void shouldThrowBadWordLengthExceptionWhenWordLengthNotEqualsFive() {
        String word = "абракадабра";
        assertThrows(BadWordLengthException.class, () -> game.tryToGuessWord(word));
    }

    @Test
    void shouldThrowWordNotFoundInDictionaryExceptionWhenWordNotFound() {
        String word = "ааббв";
        assertThrows(WordNotFoundException.class, () -> game.tryToGuessWord(word));
    }

    @Test
    void shouldReturnWonLineWhenAnswerFound()
            throws BadWordLengthException, WrongLanguageException, WordNotFoundException {

        String word = game.getAnswer();
        String result = game.tryToGuessWord(word);
        assertEquals("Вы выйграли!",result);
    }

    @Test
    void shouldReturnLoseLineWhenStepsEqualsMaxSteps()
            throws BadWordLengthException, WrongLanguageException, WordNotFoundException {

        String word = game.getAnswer().equals("топор") ? "мечта" : "топор";
        game.setSteps(5);
        String result = game.tryToGuessWord(word);
        assertEquals("Вы проиграли.. загаданное слово - " + game.getAnswer(),result);
    }

    @Test
    void shouldReturnExpectedClueLine()
            throws BadWordLengthException, WrongLanguageException, WordNotFoundException {

        game.setAnswer("барин");
        String word = "биржа";
        String result = game.tryToGuessWord(word);
        String expectedClue = "+^+-^";
        assertEquals(expectedClue,result);
    }

}
