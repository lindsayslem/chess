package service;

import dataAccess.AuthDataDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDataDAO;
import dataAccess.UserDataDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {
    private ClearService clearService;

    @BeforeEach
    public void setUp() {
        UserDataDAO userDataDAO = new UserDataDAO();
        GameDataDAO gameDataDAO = new GameDataDAO();
        AuthDataDAO authDataDAO = new AuthDataDAO();
        clearService = new ClearService(userDataDAO, gameDataDAO, authDataDAO);
    }

    @Test
    public void clearSuccess() {
        assertDoesNotThrow(() -> {clearService.clear();
        });
    }

    @Test
    public void clearFailure() {
        // mock DAO
        UserDataDAO testUserDataDAO = new UserDataDAO() {
            @Override
            public void clear() throws DataAccessException {
                throw new DataAccessException("Error clearing data");
            }
        };

        GameDataDAO testGameDataDAO = new GameDataDAO();
        AuthDataDAO testAuthDataDAO = new AuthDataDAO();
        ClearService testClearService = new ClearService(testUserDataDAO, testGameDataDAO, testAuthDataDAO);

        DataAccessException exception = assertThrows(DataAccessException.class, testClearService::clear);

        assertEquals("Error clearing data", exception.getMessage());
    }
}