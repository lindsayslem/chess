package dataaccess;

import model.GameData;

import java.util.Map;

public interface IGameDataDAO {
    GameData createGame(String gameName) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;
    Map<Integer, GameData> listGames();
    void clear();
}
