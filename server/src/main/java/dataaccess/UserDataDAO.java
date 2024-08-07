package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;


public class UserDataDAO implements IUserDataDAO {
    private final Map<String, UserData> userDataMap = new HashMap<>();

    @Override
    public UserData createUser(UserData user) throws DataAccessException {
        if(userDataMap.containsKey(user.username())){
            throw new DataAccessException("Username already exists");
        }
        userDataMap.put(user.username(), user);
        return user;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try{
            return getUser(username);
        }
        catch (Exception e){
            return null;
        }
    }

    @Override
    public void clearUser() throws DataAccessException{
        userDataMap.clear();
    }
}
