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
      "../../../../resources/word_bank.txt";
  private static final String defaultUsersFilePath =
      "../../../../resources/users.txt";

  private final String usersFilePath;
  private final String wordBankFilePath;

  private final List<String> userRecords;
  private final List<String> wordBank;

  /**
   * Initialize a new {@code TextFileRepository} with default file paths for
   * word bank and user records. Use this constructor to automatically set up
   * the repository text files.
   */
  public TextFileRepository() throws RepoException {
    this(defaultWordBankFilePath, defaultUsersFilePath);
  }

  /**
   * Initialize a new {@code TextFileRepository}.
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
      this.wordBank.add("racecar");
    } else {
      this.wordBank = this.loadFromFile(wordBankFile);
    }
    this.saveToFile(wordBank, wordBankFile);

    File usersFile = new File(this.usersFilePath);
    final boolean usersFilePathNewlyCreated = createFileIfNotExists(usersFile);
    if (usersFilePathNewlyCreated) {
      this.userRecords = new ArrayList<>();
    } else {
      this.userRecords = this.loadFromFile(usersFile);
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
    if (userRecordMatcher.groupCount() != 3) {
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
    return String.format("%s#%d#%s\n", user.getUserName(), user.getNumSuccess(),
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

  @Override
  public User addUser(String userName)
      throws InvalidUserNameException, UserExistsException, RepoException {
    final boolean isValidUserName = Pattern.matches("[a-zA-Z0-9]+", userName);
    if (!isValidUserName) {
      throw new InvalidUserNameException();
    }

    // TODO : implement
    return null;
  }

  @Override
  public void removeUser(String userName)
      throws FirstUserException, NotPermittedException, UserNotFoundException,
      RepoException {
    // TODO : implement
  }

  @Override
  public void saveUserInfo(User user)
      throws UserNotFoundException, RepoException {
    // TODO : implement
  }

  @Override
  public User getUserInfo(String userName)
      throws UserNotFoundException, RepoException {
    // TODO : implement
    return null;
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
  public void close() throws Exception {

  }
}
