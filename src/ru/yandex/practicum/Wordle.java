package ru.yandex.practicum;

import ru.yandex.practicum.exception.validate.BadWordLengthException;
import ru.yandex.practicum.exception.validate.WordNotFoundException;
import ru.yandex.practicum.exception.validate.WrongLanguageException;
import ru.yandex.practicum.util.VisibleForTesting;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Wordle {

    private final WordleGame game;
    private final Scanner scanner;
    private final PrintWriter validationLog;
    private static final DateTimeFormatter DATE_TIME_FORMATTER
            = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss");
    private final LocalDateTime dateTime;


    @VisibleForTesting
    public Wordle(PrintWriter sysWriter, PrintWriter valWriter, InputStream in) {
        validationLog = valWriter;
        var loader = new WordleDictionaryLoader(sysWriter);
        game = new WordleGame(loader, sysWriter);
        scanner = new Scanner(in);
        dateTime = LocalDateTime.now();
    }

    public Wordle(PrintWriter sysWriter, PrintWriter valWriter) {
        this(sysWriter,valWriter,System.in);;
    }


    public static void main(String[] args) {
        try (var systemWriter = new PrintWriter(
                new FileWriter("system.log", true));
             var validationWriter = new PrintWriter(
                     new FileWriter("validation.log", true))) {
            new Wordle(systemWriter, validationWriter).startGame(false);
        } catch (Exception e) {
            System.err.println("Критическая ошибка - приложение закрыто.");
        }
    }

    private void startGame(boolean isTesting) throws Exception {
        System.out.printf("%30s", "Отгадайте слово из 5 букв: ");

        while (game.isInPlay() && game.getSteps() < 6) {
            try {
                String guess = scanner.nextLine();
                guess = processEmptyInput(guess);
                processGame(guess);
                if (isTesting) {
                    break;
                }
            } catch (WordNotFoundException ex) {
                reThrowExceptionForTesting(isTesting,ex);
                System.out.printf("%30s", "Не найдено. Попробуйте еще: ");
                validationLog.printf("%s - %s%n",dateTime.format(DATE_TIME_FORMATTER),ex.getMessage());
            } catch (BadWordLengthException ex) {
                reThrowExceptionForTesting(isTesting,ex);
                System.out.printf("%30s", "Слово должно быть из 5 букв: ");
                validationLog.printf("%s - %s%n",dateTime.format(DATE_TIME_FORMATTER),ex.getMessage());
            } catch (WrongLanguageException ex) {
                reThrowExceptionForTesting(isTesting,ex);
                System.out.printf("%30s", "Только русские буквы: ");
                validationLog.printf("%s - %s%n",dateTime.format(DATE_TIME_FORMATTER),ex.getMessage());
            }
        }
        scanner.close();
    }

    @VisibleForTesting
    public void play() throws Exception {
        startGame(true);
    }

    @VisibleForTesting
    public WordleGame getGame() {
        return game;
    }

    @VisibleForTesting
    private void reThrowExceptionForTesting(boolean throwEx, Exception e) throws Exception {
        if (throwEx) {
            throw e;
        }
    }

    private String processEmptyInput(String guess) {
        if (guess.isBlank()) {
            guess = game.getSuggestedWord(game.getWord(), game.getClue());
            System.out.printf("%35s%n", "Подсказка: " + guess);
        }
        return guess;
    }

    private void processGame(String guess) throws WordNotFoundException, BadWordLengthException, WrongLanguageException {
        String gameMessage = game.tryToGuessWord(guess);
        if (game.getAttemptsLeft() > 0 && game.isInPlay()) {
            System.out.printf("%35s%n", gameMessage);
            System.out.printf("Число оставшихся попыток - %d: ", game.getAttemptsLeft());
        } else {
            System.out.print(gameMessage);
        }
    }
}
