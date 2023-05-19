package com.vlxu.coreexceptions;

/**
 * Exception that indicates an operation failing due to a user already existing.
 */
public class UserExistsException extends Exception {
  public UserExistsException(String userName) {
    super(String.format("User %s already exists", userName));
  }
}
