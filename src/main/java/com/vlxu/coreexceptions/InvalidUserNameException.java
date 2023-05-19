package com.vlxu.coreexceptions;

/**
 * Exception used to indicate the receipt of a username with invalid format.
 */
public class InvalidUserNameException extends Exception {
  public InvalidUserNameException() {
    super("Username must be consists of characters [a-zA-Z0-9] only");
  }
}
