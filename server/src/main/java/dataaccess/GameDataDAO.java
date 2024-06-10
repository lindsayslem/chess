package dataaccess;

import model.GameData;

import java.util.HashMap;
import java.util.Map;

public class GameDataDAO implements IGameDataDAO {
    private final Map<Integer, GameData> gameDataMap = new HashMap<>();
    private int nextGameID = 1;

    @Override
    public GameData createGame(String gameName) {
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
        if (!gameDataMap.containsKey(game.getGameID())) {
            throw new DataAccessException("Game not found.");
        }
        System.out.println("Updating game: " + game);
        gameDataMap.put(game.getGameID(), game);
        System.out.println("Updated game: " + gameDataMap.get(game.getGameID()));
    }

    @Override
    public Map<Integer, GameData> listGames() {
        return new HashMap<>(gameDataMap);
    }

    @Override
    public void clearGame() throws DataAccessException{
        gameDataMap.clear();
        nextGameID = 1;
    }
}
