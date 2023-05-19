package com.vlxu.entities;

import com.vlxu.coreexceptions.RepoException;

/**
 * The word repository is responsible for providing a list of words that a user
 * can guess.
 */
public interface WordRepository {
  /**
   * Returns a random word to be guessed.
   *
   * @return random word to be guessed
   * @throws RepoException see {@link RepoException}
   */
  String getRandomWord() throws RepoException;
}
