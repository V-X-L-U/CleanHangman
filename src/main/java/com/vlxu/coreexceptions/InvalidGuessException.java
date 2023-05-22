package com.vlxu.coreexceptions;

/**
 * Exception used to indicate a guess that has invalid format.
 */
public class InvalidGuessException extends Exception {
  public InvalidGuessException() {
    super("Invalid guess: a guess should be a single alphabet [a-z]");
  }
}
