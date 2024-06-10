package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class AuthDataDAOTest {

    private static MySqlUserDataDAO userDAO;
    private static MySqlAuthDataDAO authDao;
    private static MySqlGameDataDAO gameDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        userDAO = new MySqlUserDataDAO();
        authDao = new MySqlAuthDataDAO();
        gameDao = new MySqlGameDataDAO();

        userDAO.clearUser();
        authDao.clearAuth();
        gameDao.clearGame();
    }

    @Test
    @DisplayName("Create Auth - Positive")
    public void createAuthPositive() throws DataAccessException {
        UserData user = new UserData("testUser", "testPassword", "testUser@example.com");
        userDAO.createUser(user);

        AuthData auth = new AuthData("testAuthToken", "testUser");
        authDao.createAuth(auth);

        AuthData retrievedAuth = authDao.getAuth("testAuthToken");
        assertNotNull(retrievedAuth, "Auth token should be created successfully.");
        assertEquals("testUser", retrievedAuth.username(), "Auth token should be associated with the correct username.");
    }

    @Test
    @DisplayName("Create Auth - Duplicate")
    public void createAuthNegative() throws DataAccessException {
        UserData user = new UserData("testUser", "testPassword", "testUser@example.com");
        try {
            userDAO.createUser(user);
        } catch (DataAccessException e) {
            fail("Setup failed: Unable to create user for negative test.");
        }

        AuthData auth = new AuthData("testAuthToken", "testUser");
        try {
            authDao.createAuth(auth);
            authDao.createAuth(auth); // This should fail
            fail("Expected DataAccessException was not thrown.");
        } catch (DataAccessException e) {
            assertTrue(e.getMessage().contains("Duplicate entry") || e.getMessage().contains("Auth token already exists"),
                    "Exception message should indicate that the auth token already exists.");
        }
    }

    @Test
    @DisplayName("Get Auth - Positive")
    public void getAuthPositive() throws DataAccessException {
        UserData user = new UserData("testUser", "testPassword", "testUser@example.com");
        userDAO.createUser(user);
        AuthData auth = new AuthData("testAuthToken", "testUser");
        authDao.createAuth(auth);

        AuthData retrievedAuth = authDao.getAuth("testAuthToken");

        assertNotNull(retrievedAuth, "Auth token should be retrieved successfully.");
        assertEquals("testUser", retrievedAuth.username(), "Retrieved auth token should be associated with the correct username.");
    }

    @Test
    @DisplayName("Get Auth - Not found")
    public void getAuthNegative() throws DataAccessException {
        AuthData retrievedAuth = authDao.getAuth("nonExistentAuthToken");

        assertNull(retrievedAuth, "Auth token should not be found and should return null.");
    }

    @Test
    @DisplayName("Delete Auth - Positive")
    public void deleteAuthPositive() throws DataAccessException {
        UserData user = new UserData("testUser", "testPassword", "testUser@example.com");
        userDAO.createUser(user);

        AuthData auth = new AuthData("testAuthToken", "testUser");
        authDao.createAuth(auth);

        authDao.deleteAuth("testAuthToken");

        AuthData retrievedAuth = authDao.getAuth("testAuthToken");
        assertNull(retrievedAuth, "Auth token should be deleted and retrieval should return null.");

    }

    @Test
    @DisplayName("Delete Auth - authToken not found")
    public void deleteAuthNegative() {
        DataAccessException thrown = assertThrows(DataAccessException.class, () -> {
            authDao.deleteAuth("nonExistentAuthToken");
        });
        assertTrue(thrown.getMessage().contains("Auth token not found"), "Exception message should indicate that the auth token was not found.");
    }

    @Test
    @DisplayName("Clear Auth - Positive")
    public void clearAuthPositive() throws DataAccessException {
        UserData user = new UserData("testUser", "testPassword", "testUser@example.com");
        userDAO.createUser(user);
        AuthData auth1 = new AuthData("testAuthToken1", "testUser");
        AuthData auth2 = new AuthData("testAuthToken2", "testUser");
        authDao.createAuth(auth1);
        authDao.createAuth(auth2);

        // Verify the auth data was added
        assertNotNull(authDao.getAuth("testAuthToken1"), "Auth token 1 should exist.");
        assertNotNull(authDao.getAuth("testAuthToken2"), "Auth token 2 should exist.");

        // Clear the auth table
        authDao.clearAuth();

        // Verify the auth table is empty
        assertNull(authDao.getAuth("testAuthToken1"), "Auth token 1 should be cleared.");
        assertNull(authDao.getAuth("testAuthToken2"), "Auth token 2 should be cleared.");
    }
}


