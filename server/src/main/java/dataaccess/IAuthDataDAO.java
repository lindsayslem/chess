package dataaccess;

import model.AuthData;

public interface IAuthDataDAO {
    void createAuth(AuthData auth) throws DataAccessException;
    AuthData getAuth(String authToken) throws  DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    void clearAuth() throws DataAccessException;
}
