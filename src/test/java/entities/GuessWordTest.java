package entities;

import core_exceptions.InvalidGuessException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class GuessWordTest
{
    @Test
    @DisplayName("Basic Initialization test")
    void testInit()
    {
        GuessWord guessWord = new GuessWord("racecar");
        assertEquals("racecar", guessWord.getWord());
        assertEquals("-------", guessWord.getGuessView());
    }

    @Test
    @DisplayName("Single guess view update")
    void testSingleGuessViewUpdate()
    {
        GuessWord guessWord = new GuessWord("racecar");
        try
        {
            guessWord.updateGuessView('r');
        }
        catch (InvalidGuessException e)
        {
            fail("Unexpected guess exception");
        }
        assertEquals("r-----r", guessWord.getGuessView());
    }

    @Test
    @DisplayName("Multiple guess view update")
    void testMultipleGuessViewUpdate()
    {
        GuessWord guessWord = new GuessWord("racecar");
        try
        {
            guessWord.updateGuessView('r');
            assertEquals("r-----r", guessWord.getGuessView());
            guessWord.updateGuessView('e');
            assertEquals("r--e--r", guessWord.getGuessView());
            guessWord.updateGuessView('c');
            assertEquals("r-cec-r", guessWord.getGuessView());
        }
        catch (InvalidGuessException e)
        {
            fail("Unexpected guess exception");
        }
    }

    @Test
    @DisplayName("Single invalid guess")
    void testSingleInvalidGuess()
    {
        GuessWord guessWord = new GuessWord("racecar");
        assertThrows(InvalidGuessException.class, () -> guessWord.updateGuessView('1'));
        assertEquals("-------", guessWord.getGuessView());
    }

    @Test
    @DisplayName("Single wrong guess")
    void testSingleWrongGuess()
    {
        GuessWord guessWord = new GuessWord("racecar");
        try
        {
            guessWord.updateGuessView('w');
            assertEquals("-------", guessWord.getGuessView());
        }
        catch (InvalidGuessException e)
        {
            fail("Unexpected guess exception");
        }
    }

    @Test
    @DisplayName("Combined: success, wrong guess, invalid guess")
    void testCombined1()
    {
        GuessWord guessWord = new GuessWord("racecar");
        try
        {
            guessWord.updateGuessView('r');
            assertEquals("r-----r", guessWord.getGuessView());
            guessWord.updateGuessView('z');
            assertEquals("r-----r", guessWord.getGuessView());
        }
        catch (InvalidGuessException e)
        {
            fail("Unexpected guess exception");
        }

        assertThrows(InvalidGuessException.class, () -> guessWord.updateGuessView('@'));
        assertEquals("r-----r", guessWord.getGuessView());
    }

    @Test
    @Disabled("Not yet implemented")
    @DisplayName("All words must comprise of only [a-z]")
    void testCapitalLettersInWord()
    {
        // TODO : implement
    }
}
