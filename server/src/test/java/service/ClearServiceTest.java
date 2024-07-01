package service;

import dataaccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {
    private ClearService clearService;

    @BeforeEach
    public void setUp() throws DataAccessException {
        MySqlUserDataDAO userDataDAO = new MySqlUserDataDAO();
        MySqlGameDataDAO gameDataDAO = new MySqlGameDataDAO();
        MySqlAuthDataDAO authDataDAO = new MySqlAuthDataDAO();
        clearService = new ClearService(userDataDAO, gameDataDAO, authDataDAO);
    }

    @Test
    public void clearSuccess() {
        assertDoesNotThrow(() -> {clearService.clear();
        });
    }

    @Test
    public void clearFailure() throws DataAccessException {
        // mock DAO
        UserDataDAO testUserDataDAO = new UserDataDAO() {
            @Override
            public void clearUser() throws DataAccessException {
                throw new DataAccessException("Error clearing data");
            }
        };

        MySqlGameDataDAO testGameDataDAO = new MySqlGameDataDAO();
        MySqlAuthDataDAO testAuthDataDAO = new MySqlAuthDataDAO();
        ClearService testClearService = new ClearService(testUserDataDAO, testGameDataDAO, testAuthDataDAO);

        DataAccessException exception = assertThrows(DataAccessException.class, testClearService::clear);

        assertEquals("Error clearing data", exception.getMessage());
    }
}