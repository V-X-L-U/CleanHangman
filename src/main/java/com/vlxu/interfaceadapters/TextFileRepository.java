package com.vlxu.interfaceadapters;

import com.vlxu.coreexceptions.FirstUserException;
import com.vlxu.coreexceptions.InvalidUserNameException;
import com.vlxu.coreexceptions.NotPermittedException;
import com.vlxu.coreexceptions.RepoException;
import com.vlxu.coreexceptions.UserExistsException;
import com.vlxu.coreexceptions.UserNotFoundException;
import com.vlxu.entities.User;
import com.vlxu.entities.UserRepository;
import com.vlxu.entities.WordRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A text-based repository implementation.
 *
 * <p>A word bank file is used to store all words that can be guessed.
 * The first line must be an integer indicating how many words there are in the file.
 * Each word must be on a new line and be of valid format (see README for the format
 * of a valid file).</p>
 *
 * <p>A user records file is used to store all users in the system. There is currently
 * no hard limit on the number of users allowed. Instead, this will be subject to the
 * machine's file system limits. Each line must conform to "([a-zA-Z0-9]+)#([0-9]+)#([YN])\\n".
 * THe first field is the username, the second field is how many words the user has successfully
 * guessed. The last field indicates whether a user is the first user (i.e., root).
 * </p>
 */
public class TextFileRepository
    implements UserRepository, WordRepository, AutoCloseable {
  private static final String defaultWordBankFilePath =
      "src/main/resources/word_bank.txt";
  private static final String defaultUsersFilePath =
      "src/main/resources/users.txt";
  private static final String defaultWordToGuess = "racecar";
  private final String usersFilePath;
  private final String wordBankFilePath;

  private final List<User> userRecords;
  private final List<String> wordBank;

  private User signedInUser;

  /**
   * Initialize a new {@code TextFileRepository} with default file paths for
   * word bank and user records. Use this constructor to automatically set up
   * the repository text files.
   */
  public TextFileRepository() throws RepoException {
    this(defaultWordBankFilePath, defaultUsersFilePath);
  }

  /**
   * Initialize a new {@code TextFileRepository}. This repository will create
   * all files as needed, if they don't exist yet.
   *
   * @param wordBankFilePath custom word bank file path ({@code null} to use
   *                         default)
   * @param usersFilePath    custom user records file path ({@code null} to use
   *                         default)
   */
  public TextFileRepository(String wordBankFilePath, String usersFilePath)
      throws RepoException {
    this.wordBankFilePath =
        wordBankFilePath == null ? defaultWordBankFilePath : wordBankFilePath;
    this.usersFilePath =
        usersFilePath == null ? defaultUsersFilePath : usersFilePath;

    File wordBankFile = new File(this.wordBankFilePath);
    final boolean wordBankFileNewlyCreated =
        createFileIfNotExists(wordBankFile);
    if (wordBankFileNewlyCreated) {
      this.wordBank = new ArrayList<>();
      this.wordBank.add(defaultWordToGuess);
    } else {
      this.wordBank = this.loadFromFile(wordBankFile);
    }
    this.saveToFile(wordBank, wordBankFile);

    File usersFile = new File(this.usersFilePath);
    final boolean usersFilePathNewlyCreated = createFileIfNotExists(usersFile);
    this.userRecords = new ArrayList<>();
    if (!usersFilePathNewlyCreated) {
      for (String record : this.loadFromFile(usersFile)) {
        userRecords.add(instantiateUserFromRecord(record));
      }
    }
  }

  // Create the file pointed to by filePath if it doesn't exist yet.
  // Return True iff. the file was successfully created.
  private boolean createFileIfNotExists(File fileToCreate)
      throws RepoException {
    try {
      return fileToCreate.createNewFile();
    } catch (IOException e) {
      throw new RepoException(
          String.format("Could not create file: %s",
              fileToCreate.getAbsolutePath()));
    }
  }

  // Validate `userRecord` and instantiate a User object from it.
  private User instantiateUserFromRecord(String userRecord)
      throws RepoException {
    String userRecordPattern = "([a-zA-Z0-9]+)#([0-9]+)#([YN])";
    Matcher userRecordMatcher =
        Pattern.compile(userRecordPattern).matcher(userRecord);
    if (!userRecordMatcher.matches()) {
      throw new RepoException(
          String.format("Invalid user record found: %s", userRecord));
    }

    String userName = userRecordMatcher.group(1);
    int numWordsSuccessfullyGuessed =
        Integer.parseInt(userRecordMatcher.group(2));
    boolean isFirstUser = userRecordMatcher.group(3).equals("Y");

    return new User(userName, numWordsSuccessfullyGuessed, isFirstUser);
  }

  // Returns a user record from `user` and adds a newline.
  private String userRecordFromUser(User user) {
    return String.format("%s#%d#%s", user.getUserName(), user.getNumSuccess(),
        user.getIsRoot() ? "Y" : "N");
  }

  private boolean isValidWord(String word) {
    return Pattern.matches("[a-z]{7,21}", word);
  }

  private List<String> loadFromFile(File fileToLoad) throws RepoException {
    try {
      return Files.readAllLines(fileToLoad.toPath());
    } catch (IOException e) {
      throw new RepoException(String.format("Error loading records from %s",
          fileToLoad.getAbsolutePath()));
    }
  }

  private void saveToFile(List<String> fileContents, File saveFile)
      throws RepoException {
    createFileIfNotExists(saveFile);

    try {
      Files.write(saveFile.toPath(), fileContents);
    } catch (IOException e) {
      throw new RepoException(String.format("Error saving to file at %s",
          saveFile.getAbsolutePath()));
    }
  }

  // Returns the index of user `userName` in userRecords.
  private int findUser(String userName) throws UserNotFoundException {
    int i = 0;
    User userFromRecord;
    boolean userFound = false;
    while (i < this.userRecords.size() && !userFound) {
      userFromRecord = userRecords.get(i);
      userFound = userFromRecord.getUserName().equals(userName);
      i++;
    }
    i--;

    if (!userFound) {
      throw new UserNotFoundException(userName);
    }

    return i;
  }

  @Override
  public User addUser(String userName)
      throws InvalidUserNameException, UserExistsException {
    final boolean isValidUserName = Pattern.matches("[a-zA-Z0-9]+", userName);
    if (!isValidUserName) {
      throw new InvalidUserNameException();
    }

    for (User user : userRecords) {
      if (user.getUserName().equals(userName)) {
        throw new UserExistsException(userName);
      }
    }

    final boolean isFirstUser = userRecords.isEmpty();
    User newUser = new User(userName, 0, isFirstUser);
    userRecords.add(newUser);

    return newUser;
  }

  @Override
  public void removeUser(String userName)
      throws FirstUserException, NotPermittedException, UserNotFoundException,
      RepoException {
    if (signedInUser == null || !signedInUser.getIsRoot()) {
      throw new NotPermittedException(String.format("Delete user %s", userName),
          "Logged in as root user");
    }

    User userFromRecord = null;
    int indexToDelete = 0;
    boolean userFound = false;
    while (indexToDelete < userRecords.size() && !userFound) {
      userFromRecord = userRecords.get(indexToDelete);
      userFound = userFromRecord.getUserName().equals(userName);
      indexToDelete++;
    }
    indexToDelete--;

    if (!userFound) {
      throw new UserNotFoundException(userName);
    }

    if (userFromRecord.getIsRoot()) {
      throw new FirstUserException(String.format("Delete user %s", userName));
    }

    userRecords.remove(indexToDelete);
  }

  @Override
  public void saveUserInfo(User user)
      throws UserNotFoundException, NotPermittedException {
    int userIndex = findUser(user.getUserName());
    User userToUpdate = userRecords.get(userIndex);
    if (userToUpdate.getIsRoot() != user.getIsRoot()) {
      throw new NotPermittedException("Cannot change user privileges",
          "No Permissions available");
    }
    userRecords.set(userIndex, user);
  }

  @Override
  public User getUserInfo(String userName)
      throws UserNotFoundException {
    int userIndex = findUser(userName);
    return userRecords.get(userIndex);
  }

  @Override
  public String getRandomWord() throws RepoException {
    if (wordBank.isEmpty()) {
      throw new RepoException("Word bank is unexpectedly empty!");
    }

    final Random rand = new Random();
    final String selectedWord = wordBank.get(rand.nextInt(wordBank.size()));
    if (!isValidWord(selectedWord)) {
      throw new RepoException(String.format("Invalid word found at %s",
          new File(this.wordBankFilePath).getAbsolutePath()));
    }

    return selectedWord;
  }

  @Override
  public void close() throws RepoException {
    List<String> records = new ArrayList<>();
    for (User user : userRecords) {
      records.add(userRecordFromUser(user));
    }
    saveToFile(records, new File(usersFilePath));
    saveToFile(wordBank, new File(wordBankFilePath));
  }
}
