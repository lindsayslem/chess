package service;

import dataAccess.GameDataDAO;
import dataAccess.AuthDataDAO;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import chess.ChessGame;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        GameData createdGame = gameDataDAO.createGame(gameData.gameName());

        // Assign user to the game
        createdGame = new GameData(
                createdGame.gameID(),
                authData.username(),
                createdGame.blackUsername(),
                createdGame.gameName(),
                new ChessGame()
        );
        gameDataDAO.updateGame(createdGame);

        return createdGame;
    }

    public void joinGame(GameData gameData, String authToken) throws DataAccessException {
        // Validate auth token
        AuthData authData = authDataDAO.getAuth(authToken);

        // Get game
        GameData existingGame = gameDataDAO.getGame(gameData.gameID());

        // Assign user to the game
        GameData updatedGame;
        if (gameData.whiteUsername() != null && gameData.whiteUsername().equals(authData.username())) {
            updatedGame = new GameData(
                    existingGame.gameID(),
                    authData.username(),
                    existingGame.blackUsername(),
                    existingGame.gameName(),
                    existingGame.game()
            );
        } else if (gameData.blackUsername() != null && gameData.blackUsername().equals(authData.username())) {
            updatedGame = new GameData(
                    existingGame.gameID(),
                    existingGame.whiteUsername(),
                    authData.username(),
                    existingGame.gameName(),
                    existingGame.game()
            );
        } else {
            throw new DataAccessException("Invalid player color");
        }

        gameDataDAO.updateGame(updatedGame);
    }

    public List<GameData> listGames(String authToken) throws DataAccessException {
        // Validate auth token
        authDataDAO.getAuth(authToken);

        // List all games
        Map<Integer, GameData> games = gameDataDAO.listGames();
        return games.values().stream().collect(Collectors.toList());
    }
}
