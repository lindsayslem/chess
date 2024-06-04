package service;

import dataAccess.GameDataDAO;
import dataAccess.AuthDataDAO;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import chess.ChessGame;

import java.util.*;

public class GameService {
    private final GameDataDAO gameDataDAO;
    private final AuthDataDAO authDataDAO;

    public GameService(GameDataDAO gameDataDAO, AuthDataDAO authDataDAO) {
        this.gameDataDAO = gameDataDAO;
        this.authDataDAO = authDataDAO;
    }

    public GameData createGame(GameData gameData, String authToken) throws DataAccessException {
        // Validate auth token
        AuthData authData = authDataDAO.getAuth(authToken);

        // Create game
        GameData createdGame = gameDataDAO.createGame(gameData.getGameName());

        return createdGame;
    }

    public boolean joinGame(ChessGame.TeamColor playerColor, int gameID, String authToken) throws DataAccessException {
        AuthData authData = authDataDAO.getAuth(authToken);

        if (authData == null) {
            throw new DataAccessException("Unauthorized");
        }

        GameData gameData = gameDataDAO.getGame(gameID);
        if (gameData == null) {
            throw new DataAccessException("Bad request");
        }

        String username = authData.getUsername();
        if (playerColor == ChessGame.TeamColor.WHITE) {
            if (gameData.getWhiteUsername() != null) {
                return false;
            }
            gameData.setWhiteUsername(username);
        } else if (playerColor == ChessGame.TeamColor.BLACK) {
            if (gameData.getBlackUsername() != null) {
                return false;
            }
            gameData.setBlackUsername(username);
        } else {
            throw new DataAccessException("Bad request");
        }

        gameDataDAO.updateGame(gameData);
        return true;
    }

    public Map<Integer, GameData> listGames(String authToken) throws DataAccessException {
        AuthData authData = authDataDAO.getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        Map<Integer, GameData> gamesMap = gameDataDAO.listGames();
        if(gamesMap == null){
            return Collections.emptyMap();
        }
        return gamesMap;
    }
}
