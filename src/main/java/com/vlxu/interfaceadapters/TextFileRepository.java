package com.vlxu.interfaceadapters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vlxu.coreexceptions.FirstUserException;
import com.vlxu.coreexceptions.InvalidUserNameException;
import com.vlxu.coreexceptions.NotPermittedException;
import com.vlxu.coreexceptions.RepoException;
import com.vlxu.coreexceptions.UserExistsException;
import com.vlxu.coreexceptions.UserNotFoundException;
import com.vlxu.entities.User;
import com.vlxu.entities.UserRepository;
import com.vlxu.entities.WordRepository;

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
public class TextFileRepository implements UserRepository, WordRepository {
  private final String defaultWordBankFilePath = "../external_interfaces/word_bank.txt";
  private final String defaultUsersFilePath = "../external_interfaces/users.txt";

  private final String usersFilePath;
  private final String wordBankFilePath;

  public TextFileRepository() {
    this.wordBankFilePath = defaultWordBankFilePath;
    this.usersFilePath = defaultUsersFilePath;
  }

  public TextFileRepository(String wordBankFilePath, String usersFilePath) {
    this.wordBankFilePath = wordBankFilePath == null ? defaultWordBankFilePath : wordBankFilePath;
    this.usersFilePath = usersFilePath == null ? defaultUsersFilePath : usersFilePath;
  }

  // Create the file pointed to by filePath if it doesn't exist yet.
  // Return True iff. the file was successfully created.
  private boolean createFileIfNotExists(File fileToCreate) throws RepoException {
    try {
      return fileToCreate.createNewFile();
    } catch (IOException e) {
      throw new RepoException(String.format("Could not create file: %s", fileToCreate.getAbsolutePath()));
    }
  }

  // Validate `userRecord` and instantiate a User object from it.
  private User instantiateUserFromRecord(String userRecord) throws RepoException {
    String userRecordPattern = "([a-zA-Z0-9]+)#([0-9]+)#([YN])";
    Matcher userRecordMatcher = Pattern.compile(userRecordPattern).matcher(userRecord);
    if (userRecordMatcher.groupCount() != 3) {
      throw new RepoException(String.format("Invalid user record found: %s", userRecord));
    }

    String userName = userRecordMatcher.group(1);
    int numWordsSuccessfullyGuessed = Integer.parseInt(userRecordMatcher.group(2));
    boolean isFirstUser = userRecordMatcher.group(3).equals("Y");

    return new User(userName, numWordsSuccessfullyGuessed, isFirstUser);
  }

  // Search user records for a matching username.
  // Return null when user not found.
  private User findUser(String userName) throws RepoException {
    final File usersFile = new File(usersFilePath);
    final boolean usersFileNewlyCreated = createFileIfNotExists(usersFile);
    if (usersFileNewlyCreated) {
      return null;
    }
    try {
      BufferedReader wordBankReader = new BufferedReader(new FileReader(usersFile));
      String lineRead = wordBankReader.readLine();
      while (lineRead != null) {
        User userFromRecord = instantiateUserFromRecord(lineRead);
        if (userFromRecord.getUserName().equals(userName)) {
          wordBankReader.close();
          return userFromRecord;
        }
        lineRead = wordBankReader.readLine();
      }
      wordBankReader.close();
      return null;
    } catch (IOException e) {
      throw new RepoException(String.format("Could not process user records at %s", usersFile.getAbsolutePath()));
    }
  }

  // Returns a user record from `user` and adds a newline.
  private String userRecordFromUser(User user) {
    return String.format("%s#%d#%s\n", user.getUserName(), user.getNumSuccess(), user.getIsRoot() ? "Y" : "N");
  }

  private void appendToFile(String filePath, String line) throws IOException {
    BufferedWriter userRecordsWriter = new BufferedWriter(new FileWriter(filePath, true));
    userRecordsWriter.write(line);
    userRecordsWriter.close();
  }

  private boolean isValidWord(String word) {
    return Pattern.matches("[a-z]{7,21}", word);
  }

  @Override
  public User addUser(String userName) throws InvalidUserNameException, UserExistsException, RepoException {
    final boolean isValidUserName = Pattern.matches("[a-zA-Z0-9]+", userName);
    if (!isValidUserName) {
      throw new InvalidUserNameException();
    }

    User user = findUser(userName);
    if (user != null) {
      throw new UserExistsException(userName);
    }

    File usersFile = new File(usersFilePath);
    final boolean usersFileNewlyCreated = createFileIfNotExists(usersFile);
    User newUser = usersFileNewlyCreated ? new User(userName, 0, true) : new User(userName);
    try {
      appendToFile(usersFilePath, userRecordFromUser(newUser));
    } catch (IOException e) {
      throw new RepoException(String.format("Could not append user record at `%s`", usersFile.getAbsolutePath()));
    }

    return null;
  }

  @Override
  public void removeUser(String userName)
      throws FirstUserException, NotPermittedException, UserNotFoundException, RepoException {

  }

  @Override
  public void saveUserInfo(User user) throws UserNotFoundException, RepoException {

  }

  @Override
  public User getUserInfo(String userName) throws UserNotFoundException, RepoException {
    User user = findUser(userName);
    if (user == null) {
      throw new UserNotFoundException(userName);
    }
    return user;
  }

  @Override
  public String getRandomWord() throws RepoException {
    File wordBankFile = new File(wordBankFilePath);
    final boolean wordBankNewlyCreated = createFileIfNotExists(wordBankFile);
    final String defaultWordToGuess = "racecar";
    if (wordBankNewlyCreated) {
      try {
        appendToFile(wordBankFilePath, "1\n");
        appendToFile(wordBankFilePath, String.format("%s\n", defaultWordToGuess));
        return defaultWordToGuess;
      } catch (IOException e) {
        throw new RepoException(
            String.format("Could not add default word to guess at %s", wordBankFile.getAbsolutePath()));
      }
    }

    String candidateWord = "";
    try {
      BufferedReader wordBankReader = new BufferedReader(new FileReader(wordBankFilePath));
      String firstLine = wordBankReader.readLine();
      int numTotalWords = Integer.parseInt(firstLine);
      int wordSelectedIndex = (new Random().nextInt(numTotalWords)) + 1;
      candidateWord = wordBankReader.readLine();
      int currentLineIndex = 1;
      while (candidateWord != null) {
        if (!isValidWord(candidateWord)) {
          throw new RepoException(String.format("Invalid word found: %s", candidateWord));
        }
        if (currentLineIndex == wordSelectedIndex) {
          wordBankReader.close();
          return candidateWord;
        }
        candidateWord = wordBankReader.readLine();
        currentLineIndex++;
      }

      throw new RepoException(
          String.format("Selected word index %d out of bounds (total words = %d)", wordSelectedIndex,
              numTotalWords));
    } catch (Exception e) {
      if (e instanceof IOException) {
        throw new RepoException(
            String.format("Could not process word bank at %s", wordBankFile.getAbsolutePath()));
      }
      if (e instanceof NumberFormatException) {
        throw new RepoException(
            String.format("Unexpected first line for word bank at %s: %s", wordBankFile.getAbsolutePath(),
                candidateWord));
      }
      if (e instanceof RepoException) {
        throw (RepoException) e;
      }
      throw new RepoException("Unexpected Exception occurred!");
    }
  }
}
