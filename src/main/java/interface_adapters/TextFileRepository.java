package interface_adapters;

import core_exceptions.FirstUserException;
import core_exceptions.InvalidUserNameException;
import core_exceptions.NotPermittedException;
import core_exceptions.RepoException;
import core_exceptions.UserExistsException;
import core_exceptions.UserNotFoundException;
import entities.User;
import entities.UserRepository;
import entities.WordRepository;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextFileRepository implements UserRepository, WordRepository
{
    private final String defaultWordBankFilePath = "../external_interfaces/word_bank.txt";
    private final String defaultUsersFilePath = "../external_interfaces/users.txt";

    private final String usersFilePath;
    private final String wordBankFilePath;

    public TextFileRepository()
    {
        this.wordBankFilePath = defaultWordBankFilePath;
        this.usersFilePath = defaultUsersFilePath;
    }

    public TextFileRepository(String wordBankFilePath, String usersFilePath)
    {
        this.wordBankFilePath = wordBankFilePath == null ? defaultWordBankFilePath : wordBankFilePath;
        this.usersFilePath = usersFilePath == null ? defaultUsersFilePath : usersFilePath;
    }

    // Create the file pointed to by filePath if it doesn't exist yet.
    // Return True iff. the file was successfully created.
    private boolean createFileIfNotExists(File fileToCreate) throws RepoException
    {
        try
        {
            return fileToCreate.createNewFile();
        }
        catch (IOException e)
        {
            throw new RepoException(String.format("Could not create file: %s", fileToCreate.getAbsolutePath()));
        }
    }

    // Validate `userRecord` and instantiate a User object from it.
    private User instantiateUserFromRecord(String userRecord) throws RepoException
    {
        String userRecordPattern = "([a-zA-Z0-9]+)#([0-9]+)#([YN])";
        Matcher userRecordMatcher = Pattern.compile(userRecordPattern).matcher(userRecord);
        if (userRecordMatcher.groupCount() != 3)
            throw new RepoException(String.format("Invalid user Record found: %s", userRecord));

        String userName = userRecordMatcher.group();
        int numWordsSuccessfullyGuessed = Integer.parseInt(userRecordMatcher.group(1));
        boolean isFirstUser = userRecordMatcher.group(2).equals("Y");

        return new User(userName, numWordsSuccessfullyGuessed, isFirstUser);
    }

    // Search user records for a matching username.
    // Return null when user not found.
    private User findUser(String userName) throws RepoException
    {
        final File usersFile = new File(usersFilePath);
        final boolean usersFileNewlyCreated = createFileIfNotExists(usersFile);
        if (usersFileNewlyCreated)
            return null;
        try
        {
            BufferedReader wordBankReader = new BufferedReader(new FileReader(usersFile));
            String lineRead = wordBankReader.readLine();
            while (lineRead != null)
            {
                User userFromRecord = instantiateUserFromRecord(lineRead);
                if (userFromRecord.getUserName().equals(userName))
                    return userFromRecord;
                lineRead = wordBankReader.readLine();
            }
            wordBankReader.close();
        }
        catch (IOException e)
        {
            throw new RepoException(String.format("Could not create file %s", usersFile.getAbsolutePath()));
        }
        return null;
    }

    private String userRecordFromUser(User user)
    {
        return String.format("%s#%d#%s\n", user.getUserName(), user.getNumSuccess(), user.getIsRoot() ? "Y" : "N");
    }

    @Override
    public User addUser(String userName) throws InvalidUserNameException, UserExistsException, RepoException
    {
        boolean isValidUserName = Pattern.matches("[a-zA-Z0-9]+", userName);
        if (!isValidUserName) throw new InvalidUserNameException();

        User user = findUser(userName);
        if (user != null) throw new UserExistsException(userName);

        User newUser = new User(userName);
        File usersFile = new File(usersFilePath);
        createFileIfNotExists(usersFile);
        try
        {
            BufferedWriter userRecordsWriter = new BufferedWriter(new FileWriter(usersFilePath));
            userRecordsWriter.write(userRecordFromUser(newUser));
            userRecordsWriter.close();
        }
        catch (IOException e)
        {
            throw new RepoException(String.format("Could not append user record at `%s`", usersFile.getAbsolutePath()));
        }

        return null;
    }

    @Override
    public void removeUser(String userName) throws FirstUserException, NotPermittedException, UserNotFoundException, RepoException
    {

    }

    @Override
    public void saveUserInfo(User user) throws UserNotFoundException, RepoException
    {

    }

    @Override
    public User getUserInfo(String userName) throws UserNotFoundException, RepoException
    {
        User user = findUser(userName);
        if (user == null) throw new UserNotFoundException(userName);
        return user;
    }

    @Override
    public String getRandomWord() throws RepoException
    {
        return null;
    }
}
