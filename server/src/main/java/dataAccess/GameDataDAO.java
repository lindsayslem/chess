package dataAccess;

import model.GameData;

import java.util.HashMap;
import java.util.Map;

public class GameDataDAO implements IGameDataDAO {
    private final Map<Integer, GameData> gameDataMap = new HashMap<>();
    private int nextGameID = 1;

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        int gameID = nextGameID++;
        GameData gameData = new GameData(gameID, null, null, gameName, null);
        gameDataMap.put(gameID, gameData);
        return gameData;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        GameData gameData = gameDataMap.get(gameID);
        if (gameData == null) {
            throw new DataAccessException("Game not found.");
        }
        return gameData;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        if (!gameDataMap.containsKey(game.gameID())) {
            throw new DataAccessException("Game not found.");
        }
        gameDataMap.put(game.gameID(), game);
    }

    @Override
    public Map<Integer, GameData> listGames() {
        return new HashMap<>(gameDataMap);
    }

    @Override
    public void clear() {
        gameDataMap.clear();
        nextGameID = 1;
    }
}
