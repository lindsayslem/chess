package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DataAccess;
import model.GameData;
import model.AuthData;

import java.util.List;

public class GameService {
    private final DataAccess dao;

    public GameService(DataAccess dao) {
        this.dao = dao;
    }

    public GameData createGame(GameData gameData, String authToken) throws DataAccessException {
        AuthData authData = dao.getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException("Unauthorized");
        }
        gameData.setGameID(dao.getNextGameID());
        dao.createGame(gameData);
        return gameData;
    }

    public boolean joinGame(GameData gameData, String authToken) throws DataAccessException {
        GameData existingGame = dao.getGame(gameData.getGameID());
        AuthData authData = dao.getAuth(authToken);
        if (existingGame == null || authData == null) {
            throw new DataAccessException("Game or user not found");
        }
        if (gameData.getWhiteUsername() != null && existingGame.getWhiteUsername() == null) {
            existingGame.setWhiteUsername(authData.getUsername());
        } else if (gameData.getBlackUsername() != null && existingGame.getBlackUsername() == null) {
            existingGame.setBlackUsername(authData.getUsername());
        } else {
            return false; // Color is already taken or invalid request
        }
        dao.updateGame(existingGame);
        return true;
    }

    public List<GameData> listGames(String authToken) throws DataAccessException {
        AuthData authData = dao.getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException("Unauthorized");
        }
        return dao.listGames();
    }
}