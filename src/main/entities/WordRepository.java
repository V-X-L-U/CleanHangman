package src.main.entities;

import src.core_exceptions.RepoException;

public interface WordRepository {
    /**
     * Returns a random word to be guessed.
     *
     * @return random word to be guessed
     * @throws RepoException see {@link RepoException}
     */
    String getRandomWord() throws RepoException;
}
