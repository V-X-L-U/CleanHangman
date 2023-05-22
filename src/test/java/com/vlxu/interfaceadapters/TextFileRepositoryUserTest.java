package com.vlxu.interfaceadapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.vlxu.coreexceptions.InvalidUserNameException;
import com.vlxu.coreexceptions.RepoException;
import com.vlxu.coreexceptions.UserExistsException;
import com.vlxu.entities.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TextFileRepositoryUserTest {

  TextFileRepository initRepo(String wordBankFilePath, String usersFilePath) {
    try {
      return new TextFileRepository(wordBankFilePath, usersFilePath);
    } catch (RepoException e) {
      fail(String.format("Initializing repository unexpectedly failed: %s",
          e.getMessage()));
    }
    fail("Failed to initialize repository");
    return null;
  }

  void setupTestFiles(String referenceFilePath, String testFilePath) {
    File referenceFile = new File(referenceFilePath);
    File testFile = new File(testFilePath);
    try {
      Files.copy(referenceFile.toPath(), testFile.toPath(),
          StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  void assertFileContentsAreEqual(String expectedFilePath,
                                  String actualFilePath) {
    try {
      List<String> expected =
          Files.readAllLines(new File(expectedFilePath).toPath());
      List<String> actual =
          Files.readAllLines(new File(actualFilePath).toPath());
      assertEquals(expected, actual);
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  @Test
  @DisplayName("Test initializing repository without a prior file")
  void testNewlyCreated() {
    String filePath = "src/test/resources/AUTO_GEN/new_users_file.txt";
    File file = new File(filePath);
    try (TextFileRepository repo = initRepo(null, filePath)) {
      assertTrue(Files.readAllLines(file.toPath()).isEmpty());
    } catch (Exception e) {
      fail(String.format("Unexpected exception: %s", e.getMessage()));
    }
  }

  @Test
  @DisplayName("Successfully add a new user")
  void testAddUserSuccess() {
    String referenceFilePath = "src/test/resources/NO_EDIT/sample_users.txt";
    File referenceFile = new File(referenceFilePath);
    String testFilePath = "src/test/resources/AUTO_GEN/add_user_test.txt";
    File testFile = new File(testFilePath);
    try {
      Files.copy(referenceFile.toPath(), testFile.toPath(),
          StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      fail(e.getMessage());
    }

    String newUserName = "NewUser123";
    try (TextFileRepository repo = new TextFileRepository(null, testFilePath)) {
      User newUser = repo.addUser(newUserName);
      assertEquals(newUserName, newUser.getUserName());
      assertEquals(newUser.getNumSuccess(), 0);
      assertFalse(newUser.getIsRoot());
    } catch (RepoException e) {
      fail(e.getMessage());
    } catch (UserExistsException e) {
      fail("User unexpectedly exists");
    } catch (InvalidUserNameException e) {
      fail("Unexpected invalid username");
    }

    String expectedFilePath =
        "src/test/resources/NO_EDIT/add_NewUser_success.txt";
    assertFileContentsAreEqual(expectedFilePath, testFilePath);
  }

  @Test
  @DisplayName("Attempt to add a new user that already exists")
  void testAddUserAlreadyExists() {
    String referenceFilePath = "src/test/resources/NO_EDIT/sample_users.txt";
    File referenceFile = new File(referenceFilePath);
    String testFilePath =
        "src/test/resources/AUTO_GEN/add_user_exists_test.txt";
    File testFile = new File(testFilePath);
    try {
      Files.copy(referenceFile.toPath(), testFile.toPath(),
          StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      fail(e.getMessage());
    }

    String newUserName = "vallens";
    try (TextFileRepository repo = new TextFileRepository(null, testFilePath)) {
      assertThrows(UserExistsException.class, () -> repo.addUser(newUserName));
    } catch (RepoException e) {
      fail(e.getMessage());
    }

    assertFileContentsAreEqual(referenceFilePath, testFilePath);
  }

  @Test
  @DisplayName("Attempt to add a user with invalid username format")
  void testAddInvalidUserName() {
    String referenceFilePath = "src/test/resources/NO_EDIT/sample_users.txt";
    String testFilePath = "src/test/resources/AUTO_GEN/add_invalid_user.txt";
    setupTestFiles(referenceFilePath, testFilePath);

    String invalidUserName = "$%^";
    try (TextFileRepository repo = new TextFileRepository(null, testFilePath)) {
      assertThrows(InvalidUserNameException.class,
          () -> repo.addUser(invalidUserName));
    } catch (RepoException e) {
      fail(e.getMessage());
    }

    assertFileContentsAreEqual(referenceFilePath, testFilePath);
  }
}
