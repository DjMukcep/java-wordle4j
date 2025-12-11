package ru.yandex.practicum.wordledictionary;


import org.junit.jupiter.api.Test;
import ru.yandex.practicum.WordleDictionary;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MakeWordTest {


    @Test
    void testMakeWordReturnsWordFromList() {
        WordleDictionary dict = new WordleDictionary();

        dict.addWord("кот");
        dict.addWord("дом");
        dict.addWord("мир");

        String result = dict.makeWord();

        assertTrue(List.of("кот", "дом", "мир").contains(result));
    }

}
