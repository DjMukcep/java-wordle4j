package ru.yandex.practicum;

import ru.yandex.practicum.exception.system.DictionarySizeException;
import ru.yandex.practicum.exception.validate.BadWordLengthException;
import ru.yandex.practicum.exception.validate.WordNotFoundException;
import ru.yandex.practicum.exception.validate.WrongLanguageException;
import ru.yandex.practicum.util.VisibleForTesting;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordleGame {

    private static final int MAX_STEPS = 6;
    private String answer;
    private boolean isWon;
    private int steps;
    private int attemptsLeft;
    private final WordleDictionary dictionary;
    private String word;
    private String clue;
    private final Random random;
    private static final DateTimeFormatter DATE_TIME_FORMATTER
            = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss");
    private final LocalDateTime dateTime;
    private final PrintWriter systemLog;
    private final List<Character> foundLetters = new ArrayList<>();
    private final char[] plusPositions = new char[5];


    public WordleGame(WordleDictionaryLoader loader, PrintWriter systemLog) {
        dictionary = loader.wordsFileLoader(loader.getFilename());
        answer = dictionary.makeWord();
        random = new Random();
        this.systemLog = systemLog;
        dateTime = LocalDateTime.now();
    }

    public boolean isInPlay() {
        return !isWon;
    }

    public int getAttemptsLeft() {
        return attemptsLeft;
    }

    public int getSteps() {
        return steps;
    }

    public String getWord() {
        return word;
    }

    public String getClue() {
        return clue;
    }

    public String tryToGuessWord(String word)
            throws WordNotFoundException, BadWordLengthException, WrongLanguageException {

        validateWordLanguage(word);
        validateWordLength(word);
        validateWordInDictionary(word);

        this.word = word;
        if (word.equals(answer)) {
            isWon = true;
            return "Вы выйграли!";
        }
        steps++;
        attemptsLeft = MAX_STEPS - steps;

        if (attemptsLeft == 0) {
            return "Вы проиграли.. загаданное слово - " + answer;
        }
        dictionary.getDictionaryList().remove(word);
        clue = processClue(word, answer);
        return clue;
    }

    private void validateWordInDictionary(String word) throws WordNotFoundException {
        if (!dictionary.getDictionaryList().contains(word)) {
            throw new WordNotFoundException("WordNotFoundException: word not found: " + word);
        }
    }

    private void validateWordLength(String word) throws BadWordLengthException {
        if (word.length() != 5) {
            throw new BadWordLengthException(
                    "BadWordLengthException: word length is not 5: " + word);
        }
    }

    private void validateWordLanguage(String word) throws WrongLanguageException {
        if (!word.matches("[а-яА-Я-]+")) {
            throw new WrongLanguageException(
                    "WrongLanguageException: detected wrong language: "  + word);
        }
    }

    public String processClue(String word, String answer) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < answer.length(); i++) {
            String letter = answer.contains(String.valueOf(word.charAt(i))) ?
                    answer.charAt(i) == word.charAt(i) ? "+" : "^" : "-";
            builder.append(letter);
        }
        return builder.toString();
    }

    public String getSuggestedWord(String word, String clue) {
        if (word == null) {
            return updateHintOnFirstEmptyInput();
        }

        String[] clues = clue.split("");
        char[] caretPositions = processCaretPositions(clues, word);
        List<Integer> minusIndexes = processMinusPositions(clues, word, caretPositions);
        processPlusPositions(clues, word);

        removeWords(minusIndexes, word, caretPositions);

        return dictionary.getDictionaryList()
                .get(random.nextInt(dictionary.getDictionaryList().size()));
    }

    private String updateHintOnFirstEmptyInput() {
        dictionarySizeCheck();
        int randomClueWord = random.nextInt(dictionary.getDictionaryList().size());
        return dictionary.getDictionaryList().get(randomClueWord);
    }

    private void dictionarySizeCheck() {
        if (dictionary.getDictionaryList().isEmpty()) {
            systemLog.printf("%s - Dictionary is empty.%n",dateTime.format(DATE_TIME_FORMATTER));
            throw new DictionarySizeException("Словарь пустой");
        }
    }

    private void processPlusPositions(String[] clues, String word) {
        for (int i = 0; i < clues.length; i++) {
            if (clues[i].equals("+")) {
                plusPositions[i] = word.charAt(i);
            }
        }
    }

    private List<Integer> processMinusPositions(String[] clues, String word, char[] caretPositions) {
        List<Integer> minusIndexes = new ArrayList<>();

        for (int i = 0; i < clues.length; i++) {
            if (clues[i].equals("-")) {
                minusIndexes.add(i);
                caretPositions[i] = word.charAt(i);
            }
        }
        return minusIndexes;
    }


    private char[] processCaretPositions(String[] clues, String word) {
        char[] caretPositions = new char[5];
        for (int i = 0; i < clues.length; i++) {
            if (clues[i].equals("^")) {
                addFoundedLetters(word, i);
                caretPositions[i] = word.charAt(i);
            }
        }
        return caretPositions;
    }

    private void addFoundedLetters(String word, int index) {
        if (!foundLetters.contains(word.charAt(index))) {
            foundLetters.add(word.charAt(index));
        }
    }

    private void removeWords(List<Integer> minusIndexes, String word, char[] caretPositions) {
        removeWordsWithMinusIndexLetter(minusIndexes, word);
        removeWordsWithLettersDoesNotEqualsPlusPositions();
        removeWordsWhichNotContainFoundLetters();
        removeWordsWithMinusAndCaretAtPositions(caretPositions);
    }

    private void removeWordsWithMinusIndexLetter(List<Integer> minusIndexes, String word) {
        if (!minusIndexes.isEmpty()) {
            for (Integer elem : minusIndexes) {
                char ch = word.charAt(elem);
                dictionary.getDictionaryList().removeIf(w -> w.contains(String.valueOf(ch)));
            }
        }
    }

    private void removeWordsWithLettersDoesNotEqualsPlusPositions() {
        for (int i = 0; i < plusPositions.length; i++) {
            char required = plusPositions[i];
            if (required != 0) {
                int index = i;
                dictionary.getDictionaryList().removeIf(w -> w.charAt(index) != required);
            }
        }
    }

    private void removeWordsWhichNotContainFoundLetters() {
        for (Character ch : foundLetters) {
            dictionary.getDictionaryList().removeIf(w -> !w.contains(String.valueOf(ch)));
        }
    }

    private void removeWordsWithMinusAndCaretAtPositions(char[] caretPositions) {
        for (int i = 0; i < caretPositions.length; i++) {
            char required = caretPositions[i];
            if (required != 0) {
                int index = i;
                dictionary.getDictionaryList().removeIf(w -> w.charAt(index) == required);
            }
        }
    }

    @VisibleForTesting
    public String getAnswer() {
        return answer;
    }

    @VisibleForTesting
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @VisibleForTesting
    public void setSteps(int steps) {
        this.steps = steps;
    }
}
