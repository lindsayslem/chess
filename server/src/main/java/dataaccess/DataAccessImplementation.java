package dataaccess;

import model.UserData;
import model.GameData;
import model.AuthData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataAccessImplementation implements DataAccess {
    private Map<String, UserData> users = new HashMap<>();
    private Map<Integer, GameData> games = new HashMap<>();
    private Map<String, AuthData> authTokens = new HashMap<>();
    private int nextGameID = 1;

    @Override
    public void clear() {
        users.clear();
        games.clear();
        authTokens.clear();
        nextGameID = 1;
    }

    @Override
    public void insertUser(UserData user) throws DataAccessException {
        if (users.containsKey(user.getUsername())) {
            throw new DataAccessException("User already exists");
        }
        users.put(user.getUsername(), user);
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public UserData getUserByEmail(String email) {
        for(UserData user : users.values()){
            if( user.getEmail().equals(email)){
                return user;
            }
        }
        return null;
    }

    @Override
    public void createGame(GameData game) {
        games.put(game.getGameID(), game);
    }

    @Override
    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    @Override
    public List<GameData> listGames() {
        return new ArrayList<>(games.values());
    }

    @Override
    public void updateGame(GameData game) {
        games.put(game.getGameID(), game);
    }

    @Override
    public String createAuth(String username){
        String authToken = generateAuthToken();
        authTokens.put(authToken, new AuthData(authToken, username));
        return authToken;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return authTokens.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        authTokens.remove(authToken);
    }

    private String generateAuthToken(){
        return Long.toHexString(Double.doubleToLongBits(Math.random()));
    }

    @Override
    public int getNextGameID(){
        return nextGameID++;
    }
}
