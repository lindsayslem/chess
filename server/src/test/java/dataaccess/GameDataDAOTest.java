package dataaccess;

import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import chess.ChessGame;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GameDataDAOTest {

    private static MySqlUserDataDAO userDAO;
    private static MySqlAuthDataDAO authDao;
    private static MySqlGameDataDAO gameDao;

    @BeforeAll
    public static void setUpClass() throws DataAccessException {
        userDAO = new MySqlUserDataDAO();
        authDao = new MySqlAuthDataDAO();
        gameDao = new MySqlGameDataDAO();
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        userDAO.clearUser();
        authDao.clearAuth();
        gameDao.clearGame();
    }

    @AfterEach
    public void resetDatabaseAfterEachTest() throws DataAccessException {
        resetDatabase();
    }

    @Test
    @DisplayName("Create Game - Positive")
    public void createGamePositive() throws DataAccessException {
        String gameName = "testGame";
        GameData gameData = null;
        try {
            gameData = gameDao.createGame(gameName);
        } catch (DataAccessException e) {
            fail("Failed to create game: " + e.getMessage());
        }
        assertNotNull(gameData);
        assertEquals(gameName, gameData.getGameName());
    }

    @Test
    @DisplayName("Create Game - Duplicate Game")
    public void createGameNegative() throws DataAccessException {
        String gameName = "duplicateGame";

        gameDao.createGame(gameName);

        assertThrows(DataAccessException.class, () -> {
            gameDao.createGame(gameName);
        });
    }

    @Test
    @DisplayName("Get Game - Positive")
    public void getGamePositive() throws DataAccessException {
        GameData createdGame = gameDao.createGame("testGame");

        GameData retrievedGame = gameDao.getGame(createdGame.getGameID());

        assertNotNull(retrievedGame);
        assertEquals("testGame", retrievedGame.getGameName());
    }

    @Test
    @DisplayName("Get Game - Non-Existent ID")
    public void getGameNegative() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> {
            gameDao.getGame(9999);
        });
    }

    @Test
    @DisplayName("Create and List All Games")
    public void listAllGamesPositive() throws DataAccessException {
        gameDao.createGame("testGame1");
        gameDao.createGame("testGame2");

        Map<Integer, GameData> games = gameDao.listGames();
        System.out.println("All games: ");
        for (GameData game : games.values()) {
            System.out.println("Game ID: " + game.getGameID() + ", Game Name: " + game.getGameName());
        }

        assertEquals(2, games.size());
    }

    @Test
    @DisplayName("List Games - No Games Available")
    public void testListGamesNoGamesAvailable() throws DataAccessException {
        GameDataDAO dao = new GameDataDAO();
        dao.clearGame();

        Map<Integer, GameData> games = dao.listGames();

        assertTrue(games.isEmpty(), "The games list should be empty when no games are available.");
    }

    @Test
    @DisplayName("Update Game - Positive")
    public void updateGamePositive() throws DataAccessException {
        userDAO.createUser(new UserData("whitePlayer", "password123", "white@example.com"));
        userDAO.createUser(new UserData("blackPlayer", "password123", "black@example.com"));

        userDAO.createUser(new UserData("whitePlayerUpdated", "password123", "whiteUpdated@example.com"));
        userDAO.createUser(new UserData("blackPlayerUpdated", "password123", "blackUpdated@example.com"));

        GameData existingGame = gameDao.createGame("testGame");
        int gameId = existingGame.getGameID();

        GameData updatedGame = new GameData(gameId, "whitePlayerUpdated", "blackPlayerUpdated", "testGameUpdated", new ChessGame());

        gameDao.updateGame(updatedGame);

        GameData retrievedGame = gameDao.getGame(gameId);

        assertEquals("whitePlayerUpdated", retrievedGame.getWhiteUsername(), "The white player should be updated.");
        assertEquals("blackPlayerUpdated", retrievedGame.getBlackUsername(), "The black player should be updated.");
        assertEquals("testGameUpdated", retrievedGame.getGameName(), "The game name should be updated.");
    }

    @Test
    @DisplayName("Update Non-Existent Game")
    public void updateGameNegative() {
        GameData nonExistentGame = new GameData(2, "whitePlayer", "blackPlayer", "nonExistentGame", new ChessGame());
        DataAccessException exception = assertThrows(DataAccessException.class, () -> gameDao.updateGame(nonExistentGame), "Expected a DataAccessException to be thrown.");
        assertEquals("Game not found.", exception.getMessage(), "The exception message should indicate that the game was not found.");
    }

    @Test
    @DisplayName("Clear Games - Positive")
    public void clearGamesPositive() throws DataAccessException {
        GameData game1 = gameDao.createGame("testGame1");
        GameData game2 = gameDao.createGame("testGame2");

        gameDao.clearGame();

        Map<Integer, GameData> games = gameDao.listGames();
        assertTrue(games.isEmpty(), "The games list should be empty after clearing.");
    }

    private void resetDatabase() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                // Clear the games table
                stmt.executeUpdate("DELETE FROM games");
                // Clear the users table
                stmt.executeUpdate("DELETE FROM users");
                stmt.executeUpdate("ALTER TABLE games AUTO_INCREMENT = 1");
                stmt.executeUpdate("ALTER TABLE users AUTO_INCREMENT = 1");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to reset database: " + e.getMessage());
        }
    }
}


