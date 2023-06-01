package com.vlxu.usecases;

import com.vlxu.coreexceptions.RepoException;
import com.vlxu.entities.User;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShowLeaderBoardUseCaseTest
{
    boolean compareUsers(List<User> expected, List<User> actual)
    {
        if (expected.size() != actual.size()) return false;
        for (int i = 0; i < expected.size(); i++)
        {
            boolean sameUsername = expected.get(i).getUserName().equals(actual.get(i).getUserName());
            boolean sameNumSuccess = expected.get(i).getNumSuccess() == actual.get(i).getNumSuccess();
            boolean sameIsRoot = expected.get(i).getIsRoot() == actual.get(i).getIsRoot();

            if (!(sameIsRoot && sameNumSuccess && sameUsername))
                return false;
        }

        return true;
    }

    List<User> getSingleUserList()
    {
        List<User> users = new ArrayList<>();
        users.add(new User("joe", 0, false));
        return users;
    }

    List<User> getUnsorted()
    {
        List<User> users = new ArrayList<>();
        users.add(new User("a", 4, false));
        users.add(new User("c", 3, false));
        users.add(new User("b", 5, false));
        return users;
    }

    List<User> getSortedByName()
    {
        List<User> users = new ArrayList<>();
        users.add(new User("a", 4, false));
        users.add(new User("b", 5, false));
        users.add(new User("c", 3, false));
        return users;
    }

    List<User> getSortedByScore()
    {
        List<User> users = new ArrayList<>();
        users.add(new User("c", 3, false));
        users.add(new User("a", 4, false));
        users.add(new User("b", 5, false));
        return users;
    }

    GetAllUserInfo setMockReturnValue(List<User> usersList)
    {
        try
        {
            GetAllUserInfo mock = mock(GetAllUserInfo.class);
            when(mock.getAllUserInfo()).thenReturn(usersList);
            return mock;
        }
        catch (RepoException e)
        {
            fail(e.getMessage());
            return null;
        }
    }

    @Test
    @DisplayName("Test show users by name with no users")
    void testByNameEmpty()
    {
        List<User> users = new ArrayList<>();
        ShowLeaderBoardUseCase useCase = new ShowLeaderBoardUseCase(setMockReturnValue(users));
        try
        {
            assertTrue(useCase.getRankedByName().size() == 0);
        }
        catch (RepoException e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Test show users by score with no users")
    void testByScoreEmpty()
    {
        List<User> users = new ArrayList<>();
        ShowLeaderBoardUseCase useCase = new ShowLeaderBoardUseCase(setMockReturnValue(users));

        try
        {
            assertTrue(useCase.getRankedByScore().size() == 0);
        }
        catch (RepoException e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Test by name with one user")
    void testByNameSingle()
    {
        ShowLeaderBoardUseCase useCase = new ShowLeaderBoardUseCase(setMockReturnValue(getSingleUserList()));

        try
        {
            assertTrue(compareUsers(getSingleUserList(), useCase.getRankedByName()));
        }
        catch (RepoException e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Test by score with one user")
    void testByScoreSingle()
    {
        ShowLeaderBoardUseCase useCase = new ShowLeaderBoardUseCase(setMockReturnValue(getSingleUserList()));

        try
        {
            assertTrue(compareUsers(getSingleUserList(), useCase.getRankedByScore()));
        }
        catch (RepoException e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Test by name with at least 3 users")
    void testByNameMultiple()
    {
        ShowLeaderBoardUseCase useCase = new ShowLeaderBoardUseCase(setMockReturnValue(getUnsorted()));

        try
        {
            assertTrue(compareUsers(getSortedByName(), useCase.getRankedByName()));
        }
        catch (RepoException e)
        {
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Test by score with at least 3 users")
    void testByScoreMultiple()
    {
        ShowLeaderBoardUseCase useCase = new ShowLeaderBoardUseCase(setMockReturnValue(getUnsorted()));

        try
        {
            assertTrue(compareUsers(getSortedByScore(), useCase.getRankedByScore()));
        }
        catch (RepoException e)
        {
            fail(e.getMessage());
        }
    }
}
