package dataaccess;

import model.UserData;

public interface IUserDataDAO {
    UserData createUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void clearUser() throws DataAccessException;
}
