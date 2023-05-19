package com.vlxu.coreexceptions;

public class InvalidUserNameException extends Exception {
  public InvalidUserNameException() {
    super("Username must be consists of characters [a-zA-Z0-9] only");
  }
}
