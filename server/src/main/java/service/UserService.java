package service;

import dataAccess.AuthDataDAO;
import dataAccess.UserDataDAO;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    private final UserDataDAO userDataDAO;
    private final AuthDataDAO authDataDAO;

    public UserService(UserDataDAO userDataDAO, AuthDataDAO authDataDAO) {
        this.userDataDAO = userDataDAO;
        this.authDataDAO = authDataDAO;
    }

    public AuthData register(UserData user) throws DataAccessException {
        // Check if user already exists
        try {
            userDataDAO.getUser(user.getUsername());
            throw new DataAccessException("Username already taken");
        } catch (DataAccessException e) {
            // Username does not exist, proceed with registration
        }

        // Register user
        userDataDAO.createUser(user);

        // Create auth token
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, user.getUsername());
        authDataDAO.createAuth(authData);

        return authData;
    }

    public AuthData login(UserData user) throws DataAccessException {
        // Verify user credentials
        UserData storedUser = userDataDAO.getUser(user.getUsername());
        if (!storedUser.getPassword().equals(user.getPassword())) {
            throw new DataAccessException("Unauthorized");
        }

        // Create new auth token
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, user.getUsername());
        authDataDAO.createAuth(authData);

        return authData;
    }

    public void logout(String authToken) throws DataAccessException {
        // Delete auth token
        authDataDAO.deleteAuth(authToken);
    }

    public boolean validateToken(String authToken) throws DataAccessException {
        // Check if the auth token is valid
        try {
            authDataDAO.getAuth(authToken);
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }
}
