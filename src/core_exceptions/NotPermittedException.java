package src.core_exceptions;

public class NotPermittedException extends Exception {
  public NotPermittedException(String operation, String requiredPermissions) {
    super(
        String.format(
            "Operation `%s` not permitted\nPermission required: %s",
            operation, requiredPermissions));
  }
}
