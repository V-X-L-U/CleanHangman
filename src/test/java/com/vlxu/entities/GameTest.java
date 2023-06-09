package com.vlxu.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.vlxu.coreexceptions.InvalidGuessException;
import com.vlxu.coreexceptions.InvalidGuessWord;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameTest {

  private void assertGameState(
      Game game,
      String expectedGuessView,
      Set<Character> expectedLettersGuessed,
      int expectedNumWrongGuesses) {
    assertEquals(expectedGuessView, game.getGuessView());
    assertEquals(expectedLettersGuessed, game.getLettersGuessed());
    assertEquals(expectedNumWrongGuesses, game.getNumWrongGuesses());
  }
  private Game initGame(String wordToGuess) {
    try {
      return new Game(wordToGuess);
    } catch (InvalidGuessWord e) {
      fail(e.getMessage());
    }
    return null;
  }

  @Test
  @DisplayName("Test basic initialization")
  void testInit() {
    Game game = initGame("racecar");
    assertEquals(0, game.getLettersGuessed().size());
    assertEquals("-------", game.getGuessView());
    assertEquals(0, game.getNumWrongGuesses());
  }

  @Test
  @DisplayName("Single correct guess")
  void testSingleCorrectGuess() {
    Game game = initGame("racecar");
    Set<Character> expectedLettersGuessed = new HashSet<>();
    try {
      assertTrue(game.makeGuess('e'));
      expectedLettersGuessed.add('e');
      assertGameState(game, "---e---", expectedLettersGuessed, 0);
    } catch (InvalidGuessException e) {
      fail("Unexpected guess exception");
    }
  }

  @Test
  @DisplayName("Single correct guess with multiple instances")
  void testSingleCorrectGuessMultipleInstances() {
    Game game = initGame("racecar");
    Set<Character> expectedLettersGuessed = new HashSet<>();
    try {
      game.makeGuess('a');
      expectedLettersGuessed.add('a');
      assertGameState(game, "-a---a-", expectedLettersGuessed, 0);
    } catch (InvalidGuessException e) {
      fail("Unexpected guess exception");
    }
  }

  @Test
  @DisplayName("Multiple correct guess")
  void testMultipleCorrectGuesses() {
    Game game = initGame("racecar");
    Set<Character> expectedLettersGuessed = new HashSet<>();
    try {
      assertTrue(game.makeGuess('a'));
      expectedLettersGuessed.add('a');
      assertGameState(game, "-a---a-", expectedLettersGuessed, 0);
      assertTrue(game.makeGuess('e'));
      expectedLettersGuessed.add('e');
      assertGameState(game, "-a-e-a-", expectedLettersGuessed, 0);
      assertTrue(game.makeGuess('c'));
      expectedLettersGuessed.add('c');
      assertGameState(game, "-aceca-", expectedLettersGuessed, 0);
    } catch (InvalidGuessException e) {
      fail("Unexpected guess exception");
    }
  }

  @Test
  @DisplayName("Single Incorrect Guess")
  void testSingleIncorrectGuess() {
    Game game = initGame("racecar");
    Set<Character> expectedLettersGuessed = new HashSet<>();
    try {
      assertFalse(game.makeGuess('z'));
      expectedLettersGuessed.add('z');
      assertGameState(game, "-------", expectedLettersGuessed, 1);
    } catch (InvalidGuessException e) {
      fail("Unexpected guess exception");
    }
  }

  @Test
  @DisplayName("Multiple Incorrect Guess")
  void testMultipleIncorrectGuess() {
    Game game = initGame("racecar");
    Set<Character> expectedLettersGuessed = new HashSet<>();
    try {
      assertFalse(game.makeGuess('z'));
      expectedLettersGuessed.add('z');
      assertGameState(game, "-------", expectedLettersGuessed, 1);
      assertFalse(game.makeGuess('z'));
      expectedLettersGuessed.add('z');
      assertGameState(game, "-------", expectedLettersGuessed, 2);
      assertFalse(game.makeGuess('v'));
      expectedLettersGuessed.add('v');
      assertGameState(game, "-------", expectedLettersGuessed, 3);
    } catch (InvalidGuessException e) {
      fail("Unexpected guess exception");
    }
  }

  @Test
  @DisplayName("Single Invalid Guess")
  void testSingleInvalidGuess() {
    Game game = initGame("racecar");
    assertThrows(InvalidGuessException.class,
        () -> assertFalse(game.makeGuess('&')));
    // invalid guesses are not counted in lettersGuessed
    Set<Character> expectedLettersGuessed = new HashSet<>();
    assertGameState(game, "-------", expectedLettersGuessed, 0);
  }

  @Test
  @DisplayName("Combined: single incorrect, single correct, single invalid")
  void testCombined1() {
    Game game = initGame("moonlight");
    Set<Character> expectedLettersGuessed = new HashSet<>();
    try {
      assertFalse(game.makeGuess('z'));
      expectedLettersGuessed.add('z');
      assertGameState(game, "---------", expectedLettersGuessed, 1);

      assertTrue(game.makeGuess('o'));
      expectedLettersGuessed.add('o');
      assertGameState(game, "-oo------", expectedLettersGuessed, 1);
    } catch (InvalidGuessException e) {
      fail("Unexpected guess exception");
    }

    assertThrows(InvalidGuessException.class,
        () -> assertFalse(game.makeGuess(')')));
    assertGameState(game, "-oo------", expectedLettersGuessed, 1);
  }
}
