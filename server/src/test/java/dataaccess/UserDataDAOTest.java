package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserDataDAOTest {

    private static MySqlUserDataDAO userDAO;
    private static MySqlAuthDataDAO authDao;
    private static MySqlGameDataDAO gameDao;

    @BeforeAll
    static void setUpClass() throws DataAccessException {
        userDAO = new MySqlUserDataDAO();
        authDao = new MySqlAuthDataDAO();
        gameDao = new MySqlGameDataDAO();
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        userDAO.clearUser();
        authDao.clearAuth();
        gameDao.clearGame();
    }

    @Test
    @DisplayName("Create user - Positive")
    public void createUserPositive() throws DataAccessException {
        UserData user = new UserData("testUser", "testPassword", "test@example.com");
        UserData createdUser = userDAO.createUser(user);

        assertNotNull(createdUser);
        assertEquals("testUser", createdUser.username());
        assertEquals("test@example.com", createdUser.email());
        assertNotEquals("testPassword", createdUser.password());  // Password should be hashed
    }

    @Test
    @DisplayName("Create user - duplicate user")
    public void createUserNegative() throws DataAccessException {
        UserData user = new UserData("testUser", "testPassword", "test@example.com");
        userDAO.createUser(user);

        UserData newUser = new UserData("testUser", "testPassword", "test@example.com");
        assertThrows(DataAccessException.class, () -> userDAO.createUser(newUser));
    }

    @Test
    @DisplayName("Get user - Positive")
    public void getUserPositive() throws DataAccessException {
        UserData user = new UserData("testUser", "testPassword", "test@example.com");
        UserData createdUser = userDAO.createUser(user);

        UserData retrievedUser = userDAO.getUser(createdUser.username());

        assertNotNull(retrievedUser);
        assertEquals("testUser", retrievedUser.username());
    }

    @Test
    @DisplayName("Get user - nonexistent")
    public void getUserNegative() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> userDAO.getUser("nonexistent"));
    }

    @Test
    @DisplayName("Clear User - Positive")
    public void clearUserPositive() throws DataAccessException {
        UserData user = new UserData("testUser", "testPassword", "test@mail.com");
        userDAO.createUser(user);

        AuthData auth = new AuthData("testAuthToken", "testUser");
        authDao.createAuth(auth);

        GameData game1 = gameDao.createGame("testGame1");
        GameData game2 = gameDao.createGame("testGame2");

        // Act: Clear the users, auth, and games
        userDAO.clearUser();

        // Assert: Verify that the tables are empty
        assertTrue(gameDao.listGames().isEmpty(), "The games list should be empty after clearing.");
        assertNull(authDao.getAuth(auth.authToken()), "The auth data should be null after clearing.");

        // Instead of asserting null, check for DataAccessException
        assertThrows(DataAccessException.class, () -> userDAO.getUser(user.username()), "The user data should be null after clearing.");
    }
}
