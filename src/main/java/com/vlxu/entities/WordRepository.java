package com.vlxu.entities;

import com.vlxu.coreexceptions.RepoException;

public interface WordRepository {
  /**
   * Returns a random word to be guessed.
   *
   * @return random word to be guessed
   * @throws RepoException see {@link RepoException}
   */
  String getRandomWord() throws RepoException;
}
