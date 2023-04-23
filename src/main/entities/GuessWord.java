package src.main.entities;

import src.core_exceptions.InvalidGuessException;

public class GuessWord {
    private final String word;
    private String guessView;

    private final String alphabet;

    /**
     * Initialize a new guess {@code word}.
     *
     * @param word word to be guessed. Must contain only [a-zA-Z]
     */
    public GuessWord(String word) {
        this.word = word;
        this.guessView = "-".repeat(word.length());
        this.alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    }

    /**
     * Returns the word to be guessed.
     *
     * @return word to be guessed.
     */
    String getWord() {
        return word;
    }

    /**
     * Returns the current guess view. A guess view is the portion of the string
     * that has been revealed to the user, per their correct guesses.
     *
     * @return the current guess view for the word to be guessed.
     */
    String getGuessView() {
        return guessView;
    }

    /**
     * Update and return guess view given a new user guess.
     *
     * @return the updated guess view.
     * @throws InvalidGuessException If the guess is not a single alphabet [a-zA-Z]
     */
    String updateGuessView(char guess) throws InvalidGuessException {
        if (alphabet.indexOf(guess) == -1) throw new InvalidGuessException();

        StringBuilder newGuessView = new StringBuilder();

        for (int i = 0; i < guessView.length(); i++) {
            if (guessView.charAt(i) == '-') {
                if (guess == word.charAt(i)) newGuessView.append(guess);
            } else {  // already revealed
                newGuessView.append(guessView.charAt(i));
            }
        }

        guessView = newGuessView.toString();
        return guessView;
    }
}