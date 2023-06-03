package com.vlxu.usecases;

import com.vlxu.coreexceptions.InvalidGuessException;
import com.vlxu.coreexceptions.InvalidGuessWord;
import com.vlxu.coreexceptions.RepoException;
import com.vlxu.entities.Game;
import com.vlxu.entities.WordRepository;
import java.util.Set;

public class PlayGameUseCase {
  private final static int MAX_WRONG_GUESSES = 6;
  private Game game;
  private final WordRepository wordRepo;

  public PlayGameUseCase(WordRepository wordRepo) {
    this.wordRepo = wordRepo;
    game = null;
  }

  /**
   * Returns whether the game has been won.
   * <p>Win condition: less than 7 wrong guesses</p>
   */
  public boolean checkWinCondition() {
    return game.getNumWrongGuesses() < MAX_WRONG_GUESSES &&
        game.wordIsFullyGuessed();
  }

  /**
   * Returns whether the game was lost.
   * <p>Lose condition: at least 7 wrong guesses</p>
   */
  public boolean checkLoseCondition() {
    return game.getNumWrongGuesses() > MAX_WRONG_GUESSES;
  }

  public String getGuessView() {
    return game.getGuessView();
  }

  public int getNumWrongGuesses() {
    return game.getNumWrongGuesses();
  }

  public Set<Character> getLettersGuessed() {
    return game.getLettersGuessed();
  }

  public boolean makeGuess(char guess) throws InvalidGuessException {
    return game.makeGuess(guess);
  }

  /**
   * Resets the game state to a new game with a new word.
   *
   * @throws RepoException see {@link RepoException}
   * @throws InvalidGuessWord when an invalid word was detected in the word bank
   */
  public void startGame() throws RepoException, InvalidGuessWord {
    game = new Game(wordRepo.getRandomWord());
  }
}
