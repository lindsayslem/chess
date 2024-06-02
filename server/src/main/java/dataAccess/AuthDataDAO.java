package dataAccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class AuthDataDAO implements IAuthDataDAO {
    private final Map<String, AuthData> authDataMap = new HashMap<>();

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        if (authDataMap.containsKey(auth.authToken())) {
            throw new DataAccessException("Auth token already exists.");
        }
        authDataMap.put(auth.authToken(), auth);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        AuthData authData = authDataMap.get(authToken);
        if (authData == null) {
            throw new DataAccessException("Auth token not found.");
        }
        return authData;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if (!authDataMap.containsKey(authToken)) {
            throw new DataAccessException("Auth token not found.");
        }
        authDataMap.remove(authToken);
    }

    @Override
    public void clear() {
        authDataMap.clear();
    }
}
