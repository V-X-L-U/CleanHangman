package entities;

public class User
{
    private String userName;
    private int numSuccess;
    private boolean isRoot;

    /**
     * Initializes this user with {@code userName}. Number of successful guesses initialized to 0.
     */
    public User(String userName)
    {
        numSuccess = 0;
    }

    public User(String userName, int wordsSuccessfullyGuessed, boolean isRoot)
    {
        this.userName = userName;
        numSuccess = wordsSuccessfullyGuessed;
        this.isRoot = isRoot;
    }

    /**
     * Returns this user's name.
     *
     * @return this user's name.
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * Returns how many words this user has successfully guessed since account creation.
     *
     * @return this user's number of successful games.
     */
    public int getNumSuccess()
    {
        return numSuccess;
    }

    /**
     * Records that the user has successfully guessed another word.
     */
    public void incNumSuccess()
    {
        numSuccess += 1;
    }

    /**
     * Returns whether this user has root permissions.
     *
     * @return whether user has root permissions.
     */
    public boolean getIsRoot()
    {
        return isRoot;
    }
}
