package com.vlxu.usecases;

import com.vlxu.coreexceptions.RepoException;
import com.vlxu.entities.User;
import java.util.Comparator;
import java.util.List;

public class ShowLeaderBoardUseCase
{
    private GetAllUserInfo getAllUserInfo;

    public ShowLeaderBoardUseCase(GetAllUserInfo getAllUserInfo)
    {
        this.getAllUserInfo = getAllUserInfo;
    }

    /**
     * Returns a list of all users ranked alphabetically by their username.
     *
     * @return list of all users sorted alphabetically
     * @throws RepoException See {@link RepoException}.
     */
    public List<User> getRankedByName() throws RepoException
    {
        final List<User> allUsers = getAllUserInfo.getAllUserInfo();
        allUsers.sort(Comparator.comparing(User::getUserName));
        return allUsers;
    }

    /**
     * Returns a list of all users ranked by how many words they have successfully guessed (i.e., their score).
     *
     * @return list of all users sorted by score
     * @throws RepoException See {@link RepoException}.
     */
    public List<User> getRankedByScore() throws RepoException {
        final List<User> allUsers = getAllUserInfo.getAllUserInfo();
        allUsers.sort(Comparator.comparing(User::getNumSuccess));
        return allUsers;
    }
}
