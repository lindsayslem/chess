package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import chess.ChessGame;
import org.junit.jupiter.api.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameServiceTest {

    private GameService gameService;
    private IAuthDataDAO authDataDAO;
    private IGameDataDAO gameDataDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        authDataDAO = new AuthDataDAO();
        gameDataDAO = new GameDataDAO();
        gameService = new GameService(gameDataDAO, authDataDAO);
    }

    @Test
    public void createGameSuccess() throws DataAccessException {
        AuthData authData = new AuthData("authToken1", "user1");
        authDataDAO.createAuth(authData);

        // Create a new game
        GameData result = gameService.createGame("gameName", authData.authToken());

        assertNotNull(result);
        assertEquals("gameName", result.getGameName());
    }

    @Test
    public void createGameFailure() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameService.createGame("gameName", "invalidToken");
        });

        assertEquals("Auth token not found.", exception.getMessage());
    }

    @Test
    public void joinGameSuccess() throws DataAccessException {
        // Join as white
        AuthData authData = new AuthData("authToken1", "user1");
        authDataDAO.createAuth(authData);

        GameData gameData = gameDataDAO.createGame("gameName");

        boolean result = gameService.joinGame(ChessGame.TeamColor.WHITE, gameData.getGameID(), authData.authToken());

        assertTrue(result);
        assertEquals("user1", gameDataDAO.getGame(gameData.getGameID()).getWhiteUsername());
    }

    @Test
    public void joinGameFailure() throws DataAccessException {
        // Nonexistent game
        AuthData authData = new AuthData("authToken4", "user4");
        authDataDAO.createAuth(authData);

        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(ChessGame.TeamColor.WHITE, 999, authData.authToken());
        });

        assertEquals("Game not found.", exception.getMessage());
    }

    @Test
    public void listGamesSuccess() throws DataAccessException {
        // Add valid auth data
        AuthData authData = new AuthData("authToken4", "user4");
        authDataDAO.createAuth(authData);

        // Create games and add them to the DAO
        gameDataDAO.createGame("Game 1");
        gameDataDAO.createGame("Game 2");

        // List the games
        Map<Integer, GameData> games = gameService.listGames(authData.authToken());

        assertNotNull(games);
        assertEquals(2, games.size());
    }

    @Test
    public void listGamesFailure() {
        // Attempt to list games with an invalid auth token
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameService.listGames("invalidToken");
        });

        assertEquals("Auth token not found.", exception.getMessage());
    }
}
