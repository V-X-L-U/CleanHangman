package com.vlxu.usecases;

import com.vlxu.coreexceptions.RepoException;
import com.vlxu.entities.User;
import java.util.List;

public interface GetAllUserInfo
{
    /**
     * Returns a list containing all users in the game.
     *
     * @return list of all game users
     * @throws RepoException See {@link RepoException}.
     */
    public List<User> getAllUserInfo() throws RepoException;
}
