package ru.yandex.practicum.wordlegame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.WordleDictionaryLoader;
import ru.yandex.practicum.WordleGame;
import static org.junit.jupiter.api.Assertions.*;
import java.io.PrintWriter;

public class ProcessClueTest {

    private WordleGame game;

    @BeforeEach
    void setUp() {
        PrintWriter printWriter = new PrintWriter(System.out, true);
        var loader = new WordleDictionaryLoader(new PrintWriter(System.out,true));
        game = new WordleGame(loader, printWriter);
    }

    @Test
    void shouldReturnExpectedLineWithCombination1() {
        String answer = "баржа";
        String word = "баржа";
        String expected = "+++++";

        String result = game.processClue(word,answer);
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnExpectedLineWithCombination2() {
        String answer = "баржа";
        String word = "барин";
        String expected = "+++--";

        String result = game.processClue(word,answer);
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnExpectedLineWithCombination3() {
        String answer = "баржа";
        String word = "клоун";
        String expected = "-----";

        String result = game.processClue(word,answer);
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnExpectedLineWithCombination4() {
        String answer = "нечто";
        String word = "клоун";
        String expected = "--^-^";

        String result = game.processClue(word,answer);
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnExpectedLineWithCombination5() {
        String answer = "нечто";
        String word = "точно";
        String expected = "^^+^+";

        String result = game.processClue(word,answer);
        assertEquals(expected, result);
    }
}
