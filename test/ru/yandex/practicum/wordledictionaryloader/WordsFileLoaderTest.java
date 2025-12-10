package ru.yandex.practicum.wordledictionaryloader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.WordleDictionary;
import ru.yandex.practicum.WordleDictionaryLoader;
import ru.yandex.practicum.exception.system.FileNotFound;

import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class WordsFileLoaderTest {

    private WordleDictionaryLoader loader;

    @BeforeEach
    void setUp() {
        loader = new WordleDictionaryLoader(new PrintWriter(System.out));
    }

    @Test
    void shouldThrowFileNotFoundExceptionWhenNotValFilenamePassed() {
        assertThrows(FileNotFound.class, () -> loader.wordsFileLoader("unknown"));
    }

    @Test
    void shouldLoadOnlyValidFiveLetterWords() throws Exception {
        Path tempFile = Files.createTempFile("words", ".txt");
        Files.writeString(tempFile,
                "мама\n" + "привет\n" + "домик\n" + "яблоко\n" + "кошка\n",
                StandardCharsets.UTF_8);

        WordleDictionary dict = loader.wordsFileLoader(tempFile.toString());

        assertTrue(dict.getDictionaryList().contains("домик"));
        assertTrue(dict.getDictionaryList().contains("кошка"));
        assertFalse(dict.getDictionaryList().contains("мама"));
        assertFalse(dict.getDictionaryList().contains("привет"));
    }

    @Test
    void shouldReturnEmptyDictionaryForEmptyFile() throws Exception {
        Path tmp = Files.createTempFile("empty", ".txt");
        WordleDictionary dict = loader.wordsFileLoader(tmp.toString());

        assertTrue(dict.getDictionaryList().isEmpty());
    }
}
