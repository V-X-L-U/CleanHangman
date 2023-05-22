package com.vlxu.entities;

/**
 * Abstract representation of a Hangman game user.
 */
public class User {
  private final boolean isRoot;
  private String userName;
  private int numSuccess;

  /**
   * Initializes this user with {@code userName}. Number of successful guesses initialized to 0.
   */
  public User(String userName) {
    numSuccess = 0;
    isRoot = false;
  }

  /**
   * Initializes a user. Note that this user might not always be new (e.g.,
   * loaded from repository).
   *
   * @param userName                 the username for the user
   * @param wordsSuccessfullyGuessed the number of words this user has
   *                                 successfully guessed
   * @param isRoot                   whether this user has root permissions
   */
  public User(String userName, int wordsSuccessfullyGuessed, boolean isRoot) {
    // TODO : implement validation for wordsSuccessfullyGuessed
    this.userName = userName;
    numSuccess = wordsSuccessfullyGuessed;
    this.isRoot = isRoot;
  }

  /**
   * Returns this user's name.
   *
   * @return this user's name.
   */
  public String getUserName() {
    return userName;
  }

  /**
   * Returns how many words this user has successfully guessed since account creation.
   *
   * @return this user's number of successful games.
   */
  public int getNumSuccess() {
    return numSuccess;
  }

  /**
   * Records that the user has successfully guessed another word.
   */
  public void incNumSuccess() {
    numSuccess += 1;
  }

  /**
   * Returns whether this user has root permissions.
   *
   * @return whether user has root permissions.
   */
  public boolean getIsRoot() {
    return isRoot;
  }
}
