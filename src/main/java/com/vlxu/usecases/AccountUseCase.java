package com.vlxu.usecases;

import com.vlxu.coreexceptions.FirstUserException;
import com.vlxu.coreexceptions.InvalidUserNameException;
import com.vlxu.coreexceptions.NotPermittedException;
import com.vlxu.coreexceptions.RepoException;
import com.vlxu.coreexceptions.UserExistsException;
import com.vlxu.coreexceptions.UserNotFoundException;
import com.vlxu.entities.*;

public class AccountUseCase
{
    final UserRepository userRepo;

    public AccountUseCase(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User createAccount(String userName) throws InvalidUserNameException, UserExistsException, RepoException
    {
        return userRepo.addUser(userName);
    }

    public User login(String userName) throws UserNotFoundException, RepoException
    {
        userRepo.login(userName);
        return userRepo.getUserInfo(userName);
    }

    public void removeAccount(String userName) throws FirstUserException, UserNotFoundException, NotPermittedException, RepoException
    {
        userRepo.removeUser(userName);
    }
}
