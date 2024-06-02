package service;

import dataAccess.GameDataDAO;
import dataAccess.AuthDataDAO;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import chess.ChessGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        // Assign user to the game
        createdGame = new GameData(
                createdGame.getGameID(),
                authData.getUsername(),
                createdGame.getBlackUsername(),
                createdGame.getGameName(),
                new ChessGame()
        );
        gameDataDAO.updateGame(createdGame);

        return createdGame;
    }

    public boolean joinGame(ChessGame.TeamColor playerColor, Integer gameID, String authToken) throws DataAccessException {
        GameData gameData = gameDataDAO.getGame(gameID);
        AuthData authData = authDataDAO.getAuth(authToken);

        if (playerColor == ChessGame.TeamColor.WHITE) {
            if (gameData.getWhiteUsername() != null) {
                return false;
            }
            gameData.setWhiteUsername(authData.getUsername());
        } else if (playerColor == ChessGame.TeamColor.BLACK) {
            if (gameData.getBlackUsername() != null) {
                return false;
            }
            gameData.setBlackUsername(authData.getUsername());
        } else {
            throw new DataAccessException("Invalid team color");
        }

        gameDataDAO.updateGame(gameData);
        return true;
    }

    public List<GameData> listGames(String authToken) throws DataAccessException {
        AuthData authData = authDataDAO.getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        Map<Integer, GameData> gamesMap = gameDataDAO.listGames();
        return new ArrayList<>(gamesMap.values());
    }
}
