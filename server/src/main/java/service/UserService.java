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
        try{
            userDataDAO.getUser(user.username());
            throw new DataAccessException("Username already taken");
        }
        catch(DataAccessException e){
        }
        userDataDAO.createUser(user);

        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, user.username());
        authDataDAO.createAuth(authData);

        return authData;
    }

    public AuthData login(UserData user) throws DataAccessException {
        UserData storedUser = userDataDAO.getUser(user.username());
        if(!storedUser.password().equals(user.password())){
            throw new DataAccessException("Unauthorized");
        }

        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, user.username());
        authDataDAO.createAuth(authData);

        return authData;
    }

    public void logout(String authToken) throws DataAccessException {
        authDataDAO.deleteAuth(authToken);
    }
}