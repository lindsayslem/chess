package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private UserService userService;
    private IUserDataDAO userDataDAO;
    private IAuthDataDAO authDataDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        userDataDAO = new MySqlUserDataDAO();
        authDataDAO = new MySqlAuthDataDAO();
        userService = new UserService(userDataDAO, authDataDAO);

        userDataDAO.clearUser();
        authDataDAO.clearAuth();
    }

    @Test
    public void registerSuccess() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");

        AuthData authData = userService.register(user);

        assertNotNull(authData);
        assertNotNull(userDataDAO.getUser("username"));
    }

    @Test
    public void registerFailure() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        userDataDAO.createUser(user);

        assertThrows(DataAccessException.class, () -> userService.register(user));
    }

    @Test
    public void loginSuccess() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        userService.register(user);

        AuthData authData = userService.login(new UserData("username", "password", "email"));

        assertNotNull(authData);
    }

    @Test
    public void loginFailure() throws DataAccessException {
        UserData storedUser = new UserData("username", BCrypt.hashpw("correctPassword", BCrypt.gensalt()), "email");
        userDataDAO.createUser(storedUser);

        UserData loginUser = new UserData("username", "wrongPassword", "email");

        assertThrows(DataAccessException.class, () -> userService.login(loginUser));
    }

    @Test
    public void logoutSuccessAndFailure() throws DataAccessException {
        String username = "username";
        String authToken = "randomAuthToken";

        // Ensure the user exists in the users table
        userDataDAO.createUser(new UserData(username, "password", "email"));

        // Create auth token for the user
        authDataDAO.createAuth(new AuthData(authToken, username));

        // Logout the user and verify the auth token is removed
        assertDoesNotThrow(() -> userService.logout(authToken));
        assertNull(authDataDAO.getAuth(authToken));

        // Attempt to logout again and verify it fails
        assertThrows(DataAccessException.class, () -> userService.logout(authToken));
    }
}
