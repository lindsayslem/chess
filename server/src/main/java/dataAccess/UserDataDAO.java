package dataAccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class UserDataDAO implements IUserDataDAO {
    private final Map<String, UserData> userDataMap = new HashMap<>();

    @Override
    public void createUser(UserData user) throws DataAccessException {
        if(userDataMap.containsKey(user.getUsername())){
            throw new DataAccessException("Username already exists");
        }
        userDataMap.put(user.getUsername(), user);
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
    public void clear() {
        userDataMap.clear();
    }
}
