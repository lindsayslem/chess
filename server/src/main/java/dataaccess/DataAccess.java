package dataaccess;

import model.UserData;
import model.GameData;
import model.AuthData;
import java.util.List;

public interface DataAccess {
    void clear() throws DataAccessException;

    void insertUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    UserData getUserByEmail(String email) throws DataAccessException;

    void createGame(GameData game) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    List<GameData> listGames() throws DataAccessException;

    void updateGame(GameData game) throws DataAccessException;

    String createAuth(String username) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;

    int getNextGameID();
}
