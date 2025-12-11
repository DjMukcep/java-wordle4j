package ru.yandex.practicum;

import java.util.*;

public class WordleDictionary {

    private final List<String> dictionaryList;
    private final Random random;


    public WordleDictionary() {
        dictionaryList = new ArrayList<>();
        random = new Random();
    }

    public List<String> getDictionaryList() {
        return dictionaryList;
    }

    public void addWord(String word) {
        dictionaryList.add(word.toLowerCase().replaceAll("ё", "е"));
    }

    public String makeWord() {
        return dictionaryList.get(random.nextInt(dictionaryList.size()));
    }
}
