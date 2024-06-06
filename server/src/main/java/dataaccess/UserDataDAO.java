package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class UserDataDAO implements IUserDataDAO {
    private final Map<String, UserData> userDataMap = new HashMap<>();

    @Override
    public void createUser(UserData user) throws DataAccessException {
        if(userDataMap.containsKey(user.username())){
            throw new DataAccessException("Username already exists");
        }
        userDataMap.put(user.username(), user);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        UserData userData =  userDataMap.get(username);
        if(userData == null){
            throw new DataAccessException("User not found");
        }
        return userData;
    }

    @Override
    public void clearUser() throws DataAccessException{
        userDataMap.clear();
    }
}
