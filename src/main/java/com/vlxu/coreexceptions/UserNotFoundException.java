package com.vlxu.coreexceptions;

public class UserNotFoundException extends Exception {
  public UserNotFoundException(String userName) {
    super(String.format("User `%s` not found", userName));
  }
}
