package com.vlxu.coreexceptions;

public class UserExistsException extends Exception {
  public UserExistsException(String userName) {
    super(String.format("User %s already exists", userName));
  }
}
