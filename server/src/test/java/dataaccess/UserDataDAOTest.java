package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserDataDAOTest {

    private static MySqlDataAccess dao;

    @BeforeAll
    static void setUpClass() throws DataAccessException {
        dao = new MySqlDataAccess();
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        dao.clearUser();
    }

    @Test
    @DisplayName("Create user - Positive")
    public void createUserPositive() throws DataAccessException {
        UserData user = new UserData("testUser", "testPassword", "test@example.com");
        UserData createdUser = dao.createUser(user);

        assertNotNull(createdUser);
        assertEquals("testUser", createdUser.username());
        assertEquals("test@example.com", createdUser.email());
        assertNotEquals("testPassword", createdUser.password());  // Password should be hashed
    }

    @Test
    @DisplayName("Create user - duplicate user")
    public void createUserNegative() throws DataAccessException {
        UserData user = new UserData("testUser", "testPassword", "test@example.com");
        dao.createUser(user);

        UserData newUser = new UserData("testUser", "testPassword", "test@example.com");
        assertThrows(DataAccessException.class, () -> dao.createUser(newUser));
    }

    @Test
    @DisplayName("Get user - Positive")
    public void getUserPositive() throws DataAccessException {
        UserData user = new UserData("testUser", "testPassword", "test@example.com");
        UserData createdUser = dao.createUser(user);

        UserData retrievedUser = dao.getUser(createdUser.username());

        assertNotNull(retrievedUser);
        assertEquals("testUser", retrievedUser.username());
    }

    @Test
    @DisplayName("Get user - nonexistent")
    public void getUserNegative() throws DataAccessException {
        assertNull(dao.getUser("nonexistent"));
    }

}
