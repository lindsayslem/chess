package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.UserData;
import model.AuthData;

public class UserService {

    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public AuthData register(UserData user) throws DataAccessException {
        if (dataAccess.getUser(user.getUsername()) != null || dataAccess.getUserByEmail(user.getEmail()) != null) {
            return null;
        }
        dataAccess.insertUser(user);
        String authToken = dataAccess.createAuth(user.getUsername());
        return new AuthData(authToken, user.getUsername());
    }

    public AuthData login(UserData user) throws DataAccessException {
        UserData storedUser = dataAccess.getUser(user.getUsername());
        if (storedUser == null || !storedUser.getPassword().equals(user.getPassword())) {
            return null;
        }
        String authToken = dataAccess.createAuth(user.getUsername());
        return new AuthData(authToken, user.getUsername());
    }

    public boolean logout(String authToken) throws DataAccessException {
        AuthData authData = dataAccess.getAuth(authToken);
        if (authData == null) {
            return false;
        }
        dataAccess.deleteAuth(authToken);
        return true;
    }
}