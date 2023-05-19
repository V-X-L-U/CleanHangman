package com.vlxu.coreexceptions;

/**
 * Exception that indicates an operation failing because a user does not exist
 * or cannot be found.
 */
public class UserNotFoundException extends Exception {
  public UserNotFoundException(String userName) {
    super(String.format("User `%s` not found", userName));
  }
}
