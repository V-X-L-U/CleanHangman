package com.vlxu.entities;

import com.vlxu.coreexceptions.FirstUserException;
import com.vlxu.coreexceptions.InvalidUserNameException;
import com.vlxu.coreexceptions.NotPermittedException;
import com.vlxu.coreexceptions.RepoException;
import com.vlxu.coreexceptions.UserExistsException;
import com.vlxu.coreexceptions.UserNotFoundException;

/**
 * Repository responsible for storing user information.
 */
public interface UserRepository {

  /**
   * Registers a new user.
   *
   * @param userName username for the new user
   * @return a {@link User} containing information about the new user
   * @throws InvalidUserNameException when a username with invalid format is
   *                                  received
   * @throws UserExistsException      when a username has been taken
   * @throws RepoException            see {@link RepoException}
   */
  User addUser(String userName)
      throws InvalidUserNameException, UserExistsException, RepoException;

  /**
   * Removes a user. The first user to be registered can never be removed.
   * They are considered root.
   *
   * @param userName username of user that is to be removed
   * @throws FirstUserException    when an attempt to remove the first user is
   *                               made
   * @throws NotPermittedException when the user attempting user removal is
   *                               not the first user (i.e., not root)
   * @throws UserNotFoundException when {@param userName} does not exist
   * @throws RepoException         see {@link RepoException}
   */
  void removeUser(String userName)
      throws FirstUserException, NotPermittedException, UserNotFoundException,
      RepoException;

  /**
   * Update info on a user.
   *
   * @param user the user information to be saved
   * @throws UserNotFoundException when the user-to-update does not exist
   * @throws RepoException         see {@link RepoException}
   */
  void saveUserInfo(User user) throws UserNotFoundException, RepoException,
      NotPermittedException;

  /**
   * Retrieve info on user {@param userName}.
   *
   * @param userName username to look up
   * @throws UserNotFoundException when the username to lookup does not exist
   * @throws RepoException         see {@link RepoException}
   */
  User getUserInfo(String userName) throws UserNotFoundException, RepoException;

  /**
   * Authenticate a user to the repository.
   *
   * <p>Note: the initial version will only require a correct username for
   * authentication; no passwords are needed </p>
   *
   * @param userName the username of the account to log into
   * @throws UserNotFoundException when the username to log into does not exist
   * @throws RepoException         see {@link RepoException}
   */
  void login(String userName) throws UserNotFoundException, RepoException;
}
