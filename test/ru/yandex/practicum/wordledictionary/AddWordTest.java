package ru.yandex.practicum.wordledictionary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.WordleDictionary;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class AddWordTest {

    private WordleDictionary dictionary;

    @BeforeEach
    void setUp() {
        dictionary = new WordleDictionary();
    }

    @Test
    void shouldAddCorrectWord1() {
        dictionary.addWord("ёлка");
        List<String> expected = List.of("елка");
        assertEquals(expected,dictionary.getDictionaryList());
    }

    @Test
    void shouldAddCorrectWord2() {
        dictionary.addWord("МатрЁшка");
        dictionary.addWord("ЛеПёШКа");
        List<String> expected = List.of("матрешка","лепешка");
        assertEquals(expected,dictionary.getDictionaryList());
    }
}
