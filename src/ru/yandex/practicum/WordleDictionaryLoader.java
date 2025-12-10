package ru.yandex.practicum;

import ru.yandex.practicum.exception.system.FileNotFound;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class WordleDictionaryLoader {

    private final PrintWriter systemLog;
    private static final String FILENAME = "words_ru.txt";
    private static final int WORD_LENGTH = 5;
    private static final DateTimeFormatter DATE_TIME_FORMATTER
            = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss");
    private final LocalDateTime dateTime;

    public WordleDictionaryLoader(PrintWriter systemLog) {
        this.systemLog = systemLog;
        dateTime = LocalDateTime.now();
    }


    public String getFilename() {
        return FILENAME;
    }

    public WordleDictionary wordsFileLoader(String filename) {

        WordleDictionary dictionary = new WordleDictionary();

        try (var br = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
            String line;
            while (br.ready()) {
                line = br.readLine();
                if (line.length() != WORD_LENGTH) {
                    continue;
                }
                dictionary.addWord(line);
            }
        } catch (FileNotFoundException e) {
            systemLog.println(dateTime.format(DATE_TIME_FORMATTER) + " - " + e);
            throw new FileNotFound(e);

        } catch (IOException e) {
            systemLog.println(dateTime.format(DATE_TIME_FORMATTER) + " - " + e);
            throw new RuntimeException(e);
        }
        return dictionary;
    }
}
