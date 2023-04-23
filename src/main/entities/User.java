package src.main.entities;

public class User {
    private String userName;
    private int numSuccess;

    /**
     * Initializes this user with {@code userName}. Number of successful guesses initialized to 0.
     * */
    public User(String userName) {
        numSuccess = 0;
    }

    /**
     * Returns this user's name.
     *
     * @return this user's name.
     * */
    String getUserName() {
        return userName;
    }

    /**
     * Returns how many words this user has successfully guessed since account creation.
     *
     * @return this user's number of successful games.
     * */
    int getNumSuccess() {
        return numSuccess;
    }

    /**
     * Records that the user has successfully guessed another word.
     * */
    void incNumSuccess() {
        numSuccess += 1;
    }
}

