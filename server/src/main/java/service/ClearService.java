package service;

import dataAccess.AuthDataDAO;
import dataAccess.GameDataDAO;
import dataAccess.UserDataDAO;
import dataAccess.DataAccessException;

public class ClearService {
    private final UserDataDAO userDataDAO;
    private final GameDataDAO gameDataDAO;
    private final AuthDataDAO authDataDAO;

    public ClearService(UserDataDAO userDataDAO, GameDataDAO gameDataDAO, AuthDataDAO authDataDAO){
        this.userDataDAO = userDataDAO;
        this.gameDataDAO = gameDataDAO;
        this.authDataDAO = authDataDAO;
    }

    public void clear() throws DataAccessException {
        userDataDAO.clear();
        gameDataDAO.clear();
        authDataDAO.clear();
    }
}
