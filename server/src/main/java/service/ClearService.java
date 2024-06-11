package service;

import dataaccess.*;

public class ClearService {
    private final IUserDataDAO userDataDAO;
    private final IGameDataDAO gameDataDAO;
    private final IAuthDataDAO authDataDAO;

    public ClearService(IUserDataDAO userDataDAO, IGameDataDAO gameDataDAO, IAuthDataDAO authDataDAO){
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
