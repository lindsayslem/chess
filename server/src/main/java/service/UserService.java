package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    private final IUserDataDAO userDataDAO;
    private final IAuthDataDAO authDataDAO;

    public UserService(UserDataDAO userDataDAO, AuthDataDAO authDataDAO) {
        this.userDataDAO = userDataDAO;
        this.authDataDAO = authDataDAO;
    }

    public AuthData register(UserData user) throws DataAccessException {
        // Check if user already exists
        try {
            userDataDAO.getUser(user.username());
            throw new DataAccessException("Username already taken");
        } catch (DataAccessException e) {
            // Username does not exist, proceed with registration
        }

        // Register user
        userDataDAO.createUser(user);

        // Create auth token
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, user.username());
        authDataDAO.createAuth(authData);

        return authData;
    }

    public AuthData login(UserData user) throws DataAccessException {
        // Verify user credentials
        UserData storedUser = userDataDAO.getUser(user.username());
        if (!storedUser.password().equals(user.password())) {
            throw new DataAccessException("Unauthorized");
        }

        // Create new auth token
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, user.username());
        authDataDAO.createAuth(authData);

        return authData;
    }

    public void logout(String authToken) throws DataAccessException {
        // Delete auth token
        authDataDAO.deleteAuth(authToken);
    }

}
