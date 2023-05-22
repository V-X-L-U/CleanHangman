package com.vlxu.coreexceptions;

public class InvalidGuessWord extends Exception {
  public InvalidGuessWord(String wordToGuess) {
    super(String.format("Invalid guess word: %s", wordToGuess));
  }
}
