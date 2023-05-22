package com.vlxu.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import com.vlxu.coreexceptions.InvalidGuessException;
import com.vlxu.coreexceptions.InvalidGuessWord;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GuessWordTest {
  GuessWord initGuessWord(String guessWord) {
    try {
      return new GuessWord(guessWord);
    } catch (InvalidGuessWord e) {
      fail(e.getMessage());
    }
    return null;
  }

  @Test
  @DisplayName("Basic Initialization test")
  void testInit() {
    GuessWord guessWord = initGuessWord("racecar");
    assertEquals("racecar", guessWord.getWord());
    assertEquals("-------", guessWord.getGuessView());
  }

  @Test
  @DisplayName("Single guess view update")
  void testSingleGuessViewUpdate() {
    GuessWord guessWord = initGuessWord("racecar");
    try {
      guessWord.updateGuessView('r');
    } catch (InvalidGuessException e) {
      fail("Unexpected guess exception");
    }
    assertEquals("r-----r", guessWord.getGuessView());
  }

  @Test
  @DisplayName("Multiple guess view update")
  void testMultipleGuessViewUpdate() {
    GuessWord guessWord = initGuessWord("racecar");
    try {
      guessWord.updateGuessView('r');
      assertEquals("r-----r", guessWord.getGuessView());
      guessWord.updateGuessView('e');
      assertEquals("r--e--r", guessWord.getGuessView());
      guessWord.updateGuessView('c');
      assertEquals("r-cec-r", guessWord.getGuessView());
    } catch (InvalidGuessException e) {
      fail("Unexpected guess exception");
    }
  }

  @Test
  @DisplayName("Single invalid guess")
  void testSingleInvalidGuess() {
    GuessWord guessWord = initGuessWord("racecar");
    assertThrows(InvalidGuessException.class,
        () -> guessWord.updateGuessView('1'));
    assertEquals("-------", guessWord.getGuessView());
  }

  @Test
  @DisplayName("Single wrong guess")
  void testSingleWrongGuess() {
    GuessWord guessWord = initGuessWord("racecar");
    try {
      guessWord.updateGuessView('w');
      assertEquals("-------", guessWord.getGuessView());
    } catch (InvalidGuessException e) {
      fail("Unexpected guess exception");
    }
  }

  @Test
  @DisplayName("Combined: success, wrong guess, invalid guess")
  void testCombined1() {
    GuessWord guessWord = initGuessWord("racecar");
    try {
      guessWord.updateGuessView('r');
      assertEquals("r-----r", guessWord.getGuessView());
      guessWord.updateGuessView('z');
      assertEquals("r-----r", guessWord.getGuessView());
    } catch (InvalidGuessException e) {
      fail("Unexpected guess exception");
    }

    assertThrows(InvalidGuessException.class,
        () -> guessWord.updateGuessView('@'));
    assertEquals("r-----r", guessWord.getGuessView());
  }

  @Test
  @DisplayName("Capital letters in word-to-guess should be invalid")
  void testCapitalLettersInWord() {
    assertThrows(InvalidGuessWord.class, () -> new GuessWord("Racecar"));
  }
}
