package com.vlxu.entities;

import com.vlxu.coreexceptions.InvalidGuessException;
import com.vlxu.coreexceptions.InvalidGuessWord;
import java.util.regex.Pattern;

/**
 * Abstract representation of a word to be guessed.
 *
 * <p>
 * The word-to-guess refers to the actual word being guessed (e.g., "racecar").
 * The guess view refers to the portion of the word-to-guess that a user has
 * revealed (e.g., "---e---" if 'e' was correctly guessed).
 * </p>
 *
 * <p>The word-to-guess must be a word of length 7-21 characters, consisting
 * of only the letters [a-z]. Any guesses outside of [a-z] is considered
 * invalid.
 * </p>
 */
public class GuessWord {
  private final String word;
  private final String alphabet;
  private String guessView;

  /**
   * Initialize a new guess {@code word}.
   *
   * @param word word to be guessed.
   */
  public GuessWord(String word) throws InvalidGuessWord {
    this.word = word;
    if (!isValidGuessWord(word)) {
      throw new InvalidGuessWord(word);
    }
    this.guessView = "-".repeat(word.length());
    this.alphabet = "abcdefghijklmnopqrstuvwxyz";
  }


  /**
   * Returns True iff. {@param guessWord} is a valid guess word. See
   * {@link GuessWord} for details of a valid guess word.
   *
   * @param guessWord the word to be checked
   * @return whether a word is a valid guess word
   */
  public static boolean isValidGuessWord(String guessWord) {
    return Pattern.matches("^[a-z]{7,21}$", guessWord);
  }

  /**
   * Returns the word to be guessed.
   *
   * @return word to be guessed.
   */
  public String getWord() {
    return word;
  }

  /**
   * Returns the current guess view. A guess view is the portion of the string that has been
   * revealed to the user, per their correct guesses.
   *
   * @return the current guess view for the word to be guessed.
   */
  public String getGuessView() {
    return guessView;
  }

  /**
   * Updates the guess view and returns whether it has changed.
   *
   * @return whether a letter was correctly guessed
   * @throws InvalidGuessException if an invalid guess was made. See {@link GuessWord}.
   */
  public boolean updateGuessView(char guess) throws InvalidGuessException {
    if (alphabet.indexOf(guess) == -1) {
      throw new InvalidGuessException();
    }

    StringBuilder newGuessView = new StringBuilder();

    boolean guessedCorrectly = false;
    for (int i = 0; i < guessView.length(); i++) {
      if (guessView.charAt(i) == '-') {
        if (guess == word.charAt(i)) {
          guessedCorrectly = true;
          newGuessView.append(guess);
        } else {
          newGuessView.append('-');
        }
      } else { // already revealed
        newGuessView.append(guessView.charAt(i));
      }
    }

    guessView = newGuessView.toString();
    return guessedCorrectly;
  }
}
