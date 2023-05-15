package src.main.entities;

import java.util.HashSet;
import java.util.Set;
import src.core_exceptions.InvalidGuessException;

public class Game {
  private final GuessWord guessWord;
  private final Set<Character> lettersGuessed;
  private int numWrongGuesses;

  /** Initialize a new game with the word to guess being {@code wordToGuess}. */
  public Game(String wordToGuess) {
    guessWord = new GuessWord(wordToGuess);
    lettersGuessed = new HashSet<>();
    numWrongGuesses = 0;
  }

  /**
   * Return True iff. {@code guess} is contained in the word to guess. Records the guess and updates
   * the guess view. Records whether the guess was correct.
   *
   * @return whether the guess was correct
   */
  public boolean makeGuess(char guess) throws InvalidGuessException {
    boolean guessCorrect = guessWord.updateGuessView(guess);
    lettersGuessed.add(guess);
    if (!guessCorrect) numWrongGuesses += 1;
    return guessCorrect;
  }

  /**
   * Returns a copy of the letters already guessed in this game.
   *
   * @return list of all letters already guessed
   */
  public Set<Character> getLettersGuessed() {
    return new HashSet<>(lettersGuessed);
  }

  /**
   * Returns the current guess view.
   *
   * @return the current guess view for this game.
   */
  public String getGuessView() {
    return guessWord.getGuessView();
  }

  /**
   * Returns the number of guesses the user has gotten wrong in the game. Wrong letters that have
   * been guessed still count as a wrong guess. Invalid guesses are not counted.
   *
   * @return how many guesses were wrong
   */
  public int getNumWrongGuesses() {
    return numWrongGuesses;
  }
}
