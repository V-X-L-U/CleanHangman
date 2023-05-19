package com.vlxu.coreexceptions;

/**
 * An exception that indicates a user attempting an operation for which they
 * do not have permissions to do so.
 */
public class NotPermittedException extends Exception {
  /**
   * Instantiates a new {@code NotPermittedException}.
   *
   * @param operation the operation that failed due to insufficient permissions
   * @param requiredPermissions the permission required for {@code operation}
   */
  public NotPermittedException(String operation, String requiredPermissions) {
    super(
        String.format(
            "Operation `%s` not permitted\nPermission required: %s",
            operation, requiredPermissions));
  }
}
