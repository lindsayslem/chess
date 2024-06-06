package service;

import dataaccess.AuthDataDAO;
import dataaccess.GameDataDAO;
import dataaccess.UserDataDAO;
import dataaccess.DataAccessException;

public class ClearService {
    private final UserDataDAO userDataDAO;
    private final GameDataDAO gameDataDAO;
    private final AuthDataDAO authDataDAO;

    public ClearService(UserDataDAO userDataDAO, GameDataDAO gameDataDAO, AuthDataDAO authDataDAO){
        this.userDataDAO = userDataDAO;
        this.gameDataDAO = gameDataDAO;
        this.authDataDAO = authDataDAO;
    }

    // clears data
    public void clear() throws DataAccessException {
        userDataDAO.clearUser();
        gameDataDAO.clearGame();
        authDataDAO.clearAuth();
    }
}
