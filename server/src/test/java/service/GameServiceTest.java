package service;

import dataAccess.AuthDataDAO;
import dataAccess.GameDataDAO;
import model.AuthData;
import model.GameData;
import chess.ChessGame;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameServiceTest {

    private GameService gameService;
    private AuthDataDAO authDataDAO;
    private GameDataDAO gameDataDAO;


    @BeforeEach
    public void setUp() {
        authDataDAO = new AuthDataDAO();
        gameDataDAO = new GameDataDAO();
        gameService = new GameService(gameDataDAO, authDataDAO);
    }

    @Test
    public void createGameSuccess() throws DataAccessException {
        // add valid auth data
        AuthData authData = new AuthData("authToken1", "user1");
        authDataDAO.createAuth(authData);

        // create a new game
        GameData gameData = new GameData(1, null, null, "gameName", new ChessGame());
        GameData result = gameService.createGame(gameData, authData.getAuthToken());

        assertNotNull(result);
        assertEquals("Test Game", result.getGameName());
    }

    @Test
    public void createGameFailure() {
        // attempt to create a game with an invalid auth token
        GameData gameData = new GameData(1, null, null, "gameName", new ChessGame());

        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameService.createGame(gameData, "invalidToken");
        });

        assertEquals("Auth token not found.", exception.getMessage());
    }

    @Test
    public void joinGameSuccess() throws DataAccessException {
        // join as white
        AuthData authData = new AuthData("authToken1", "user1");
        authDataDAO.createAuth(authData);

        GameData gameData = new GameData(1, null, null, "gameName", new ChessGame());
        gameDataDAO.createGame("gameName");

        boolean result = gameService.joinGame(ChessGame.TeamColor.WHITE, 1, "authToken1");

        assertTrue(result);
        assertEquals("user1", gameDataDAO.getGame(1).getWhiteUsername());
    }

    @Test
    public void joinGameFailure() throws DataAccessException {
        // nonexistent game
        AuthData authData = new AuthData("authToken4", "user4");
        authDataDAO.createAuth(authData);

        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(ChessGame.TeamColor.WHITE, 999, "authToken4");
        });

        assertEquals("Game not found.", exception.getMessage());
    }

    @Test
    public void listGamesSuccess() throws DataAccessException {
        // add valid auth data
        AuthData authData = new AuthData("authToken4", "user4");
        authDataDAO.createAuth(authData);

        // create games and add them to the DAO
        GameData gameData1 = new GameData(4, "user4", null, "Game 1", new ChessGame());
        GameData gameData2 = new GameData(5, null, "user4", "Game 2", new ChessGame());
        gameDataDAO.createGame("Game1");
        gameDataDAO.createGame("Game2");

        // list the games
        Map<Integer, GameData> games = gameService.listGames(authData.getAuthToken());

        assertNotNull(games);
        assertEquals(2, games.size());
    }

    @Test
    public void listGamesFailure() {
        // attempt to list games with an invalid auth token
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameService.listGames("invalidToken");
        });

        assertEquals("Auth token not found.", exception.getMessage());
    }

}
