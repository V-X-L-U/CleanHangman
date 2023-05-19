package com.vlxu.coreexceptions;

/**
 * The first user to register is automatically given root permissions.
 * This exception indicates operations that violate a user's first-user status
 * (e.g., the first user can never be deleted).
 */
public class FirstUserException extends Exception {
  public FirstUserException(String operation) {
    super(String.format("Operation `%s` not allowed for first user", operation));
  }
}
