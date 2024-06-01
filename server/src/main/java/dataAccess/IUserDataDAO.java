package dataAccess;

import model.UserData;

public interface IUserDataDAO {
    void createUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void clear();
}
