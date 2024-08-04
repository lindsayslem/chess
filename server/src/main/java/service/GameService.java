package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import chess.ChessGame;

import java.util.Collections;
import java.util.Map;

public class GameService {
    private final IGameDataDAO gameDataDAO;
    private final IAuthDataDAO authDataDAO;

    public GameService(IGameDataDAO gameDataDAO, IAuthDataDAO authDataDAO) {
        this.gameDataDAO = gameDataDAO;
        this.authDataDAO = authDataDAO;
    }

    public GameData createGame(GameData gameData, String authToken) throws DataAccessException {
        System.out.println("Starting game creation with authToken: " + authToken);
        AuthData authData = authDataDAO.getAuth(authToken);
        System.out.println("Auth Data: " + authData);
        if (authData == null) {
            System.out.println("Error: unauthorized - Auth token is null");
            throw new DataAccessException("Error: unauthorized");
        }
        System.out.println("Auth token is valid. Creating game...");
        System.out.println("Received game data: " + gameData);
        try {
            // Insert game data into the database
            gameDataDAO.createGame(gameData.getGameName());
            System.out.println("Game created successfully.");
        } catch (Exception e) {
            System.out.println("Exception while creating game: " + e.getMessage());
            throw new DataAccessException("Error creating game: " + e.getMessage());
        }

        return gameData;
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

        String username = authData.username();
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
        System.out.println("List of Games: " + gamesMap);
        if(gamesMap == null){
            return Collections.emptyMap();
        }
        return gamesMap;
    }
}
