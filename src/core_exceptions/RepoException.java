package src.core_exceptions;

/**
 * Thrown when a repository fails to perform an operation due to reasons that are beyond the scope
 * of business rules (i.e., errors/exceptions that are implementation specific). This is most often
 * caused by IO-related issues in the repository implementation.
 */
public class RepoException extends Exception {
  public RepoException(String cause) {
    super(cause);
  }
}
