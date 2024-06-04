package service;

import dataAccess.AuthDataDAO;
import dataAccess.UserDataDAO;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private UserService userService;
    private TestUserDataDAO userDataDAO;
    private TestAuthData1DAO authDataDAO;

    @BeforeEach
    public void setUp() {
        userDataDAO = new TestUserDataDAO();
        authDataDAO = new TestAuthData1DAO();
        userService = new UserService(userDataDAO, authDataDAO);
    }

    @Test
    public void registerSuccess() throws DataAccessException{
        UserData user = new UserData("username", "password", "email");
        userDataDAO.setUser(null);

        AuthData authData = userService.register(user);

        assertNotNull(authData);
        assertEquals(user, userDataDAO.getUser("username"));
    }

    @Test
    public void registerFailure() throws DataAccessException{
        //user already exists
        UserData user = new UserData("username", "password", "email");
        userDataDAO.setUser(user);

        assertThrows(DataAccessException.class, () -> userService.register(user));
    }

    @Test
    public void loginSuccess() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        userDataDAO.setUser(user);

        AuthData authData = userService.login(user);

        assertNotNull(authData);
    }

    @Test
    public void loginFailure() throws DataAccessException {
        // wrong password login
        UserData user = new UserData("username", "password", "email");
        UserData storedUser = new UserData("username", "wrongPassword", "email");
        userDataDAO.setUser(storedUser);

        assertThrows(DataAccessException.class, () -> userService.login(user));
    }

    @Test
    public void logoutSuccessAndFailure() throws DataAccessException {
        String authToken = "randomAuthToken";
        authDataDAO.setAuth(new AuthData(authToken, "username"));

        assertDoesNotThrow(() -> userService.logout(authToken));
        assertNull(authDataDAO.getAuth(authToken));

        // already logged out
        assertThrows(DataAccessException.class, () -> userService.logout(authToken));
    }
}

class TestUserDataDAO extends UserDataDAO{
    private UserData user;

    public void setUser(UserData user){
        this.user = user;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException{
        if(user != null && user.getUsername().equals(username)){
            return user;
        }
        throw new DataAccessException("User not found");
    }

    @Override
    public void createUser(UserData user) throws DataAccessException{
        if(this.user != null && this.user.getUsername().equals(user.getUsername())){
            throw new DataAccessException("User already exists");
        }
        this.user = user;
    }
}

class TestAuthData1DAO extends AuthDataDAO{
    private AuthData authData;

    public void  setAuth(AuthData authData){
        this.authData = authData;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException{
        if(authData != null && authData.getAuthToken().equals(authToken)){
            return authData;
        }
        return null;
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException{
        this.authData = authData;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException{
        if(authData != null && authData.getAuthToken().equals(authToken)){
            authData = null;
        }
        else{
            throw new DataAccessException("Auth token not found");
        }
    }
}