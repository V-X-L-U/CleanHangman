package com.vlxu.interfaceadapters;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.vlxu.coreexceptions.RepoException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TextFileRepositoryGetRandomWordTest {

  TextFileRepository initRepo(String wordBankFilePath, String usersFilePath) {
    try {
      return new TextFileRepository(wordBankFilePath, usersFilePath);
    } catch (RepoException e) {
      fail(String.format("Initializing repository unexpectedly failed: %s",
          e.getMessage()));
    }
    fail("Failed to initialize repository");
    return null;
  }

  @Test
  @DisplayName("Test getting random word from non-existent file")
  void testNewlyCreated() {
    String filePath =
        "src/test/resources/AUTO_GEN/random_word_new_word_bank.txt";
    File testFile = new File(filePath);
    testFile.delete();


    try (TextFileRepository repo = initRepo(filePath, null)) {
      try {
        // test that the default word to guess is returned
        String wordGotten = repo.getRandomWord();
        assertEquals("racecar", wordGotten);
      } catch (RepoException e) {
        fail(e.getMessage());
      }

      // check file contents are as expected
      String expectedContentsFilePath =
          "src/test/resources/NO_EDIT/expected_new_random_word_bank.txt";
      try {
        byte[] expected =
            Files.readAllBytes(new File(expectedContentsFilePath).toPath());
        byte[] actual = Files.readAllBytes(testFile.toPath());
        assertArrayEquals(expected, actual);
      } catch (IOException e) {
        fail(e.getMessage());
      }
    } catch (Exception e) {
      fail("Unexpected exception");
    }

  }

  @Test
  @DisplayName("Test correct first line but word is less than 7 characters")
  void testWordTooShort() throws Exception {
    final String filePath = "src/test/resources/NO_EDIT/word_too_short.txt";
    try (TextFileRepository repo = initRepo(filePath, null)) {
      assertThrows(RepoException.class, repo::getRandomWord);
    }
  }

  @Test
  @DisplayName("Test getting random word from existing file")
  void testGetRandomWordSuccess() {
    final String filePath = "src/test/resources/NO_EDIT/3random_words.txt";
    TextFileRepository repo = initRepo(filePath, null);
    try {
      String wordGotten = repo.getRandomWord();
      List<String> wordsInBank = new ArrayList<>();
      wordsInBank.add("moonlight");
      wordsInBank.add("sunshine");
      wordsInBank.add("goodbye");
      assertTrue(wordsInBank.contains(wordGotten),
          String.format("Received %s instead", wordGotten));
    } catch (RepoException e) {
      fail(e.getMessage());
    }
  }
}
