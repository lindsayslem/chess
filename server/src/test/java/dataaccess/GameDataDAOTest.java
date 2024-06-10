package dataaccess;

import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import chess.ChessGame;
import org.slf4j.MDC;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GameDataDAOTest {

    private static MySqlDataAccess dao;

    @BeforeAll
    public static void setUpClass() throws DataAccessException {
        dao = new MySqlDataAccess();
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        dao.clearGame();
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
            gameData = dao.createGame(gameName);
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

        dao.createGame(gameName);

        assertThrows(DataAccessException.class, () -> {
            dao.createGame(gameName);
        });
    }

    @Test
    @DisplayName("Get Game - Positive")
    public void getGamePositive() throws DataAccessException {
        GameData createdGame = dao.createGame("testGame");

        GameData retrievedGame = dao.getGame(createdGame.getGameID());

        assertNotNull(retrievedGame);
        assertEquals("testGame", retrievedGame.getGameName());
    }

    @Test
    @DisplayName("Get Game - Non-Existent ID")
    public void getGameNegative() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> {
            dao.getGame(9999);
        });
    }

    @Test
    @DisplayName("Create and List All Games")
    public void listAllGamesPositive() throws DataAccessException {
        dao.createGame("testGame1");
        dao.createGame("testGame2");

        Map<Integer, GameData> games = dao.listGames();
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
        dao.createUser(new UserData("whitePlayer", "password123", "white@example.com"));
        dao.createUser(new UserData("blackPlayer", "password123", "black@example.com"));

        dao.createUser(new UserData("whitePlayerUpdated", "password123", "whiteUpdated@example.com"));
        dao.createUser(new UserData("blackPlayerUpdated", "password123", "blackUpdated@example.com"));

        GameData existingGame = dao.createGame("testGame");
        int gameId = existingGame.getGameID();

        GameData updatedGame = new GameData(gameId, "whitePlayerUpdated", "blackPlayerUpdated", "testGameUpdated", new ChessGame());

        dao.updateGame(updatedGame);

        GameData retrievedGame = dao.getGame(gameId);

        assertEquals("whitePlayerUpdated", retrievedGame.getWhiteUsername(), "The white player should be updated.");
        assertEquals("blackPlayerUpdated", retrievedGame.getBlackUsername(), "The black player should be updated.");
        assertEquals("testGameUpdated", retrievedGame.getGameName(), "The game name should be updated.");
    }

    @Test
    @DisplayName("Update Non-Existent Game")
    public void updateGameNegative() {
        GameData nonExistentGame = new GameData(2, "whitePlayer", "blackPlayer", "nonExistentGame", new ChessGame());
        DataAccessException exception = assertThrows(DataAccessException.class, () -> dao.updateGame(nonExistentGame), "Expected a DataAccessException to be thrown.");
        assertEquals("Game not found.", exception.getMessage(), "The exception message should indicate that the game was not found.");
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

    @Test
    public void testCreateGameTransaction() {
        String gameName = "Transactional Game";

        try {
            GameData game = dao.createGame(gameName);
            assertNotNull(game, "Game should be created successfully");
            assertEquals(gameName, game.getGameName(), "Game name should match the input");

            // Further assertions to verify that the game was saved to the database can be added here
        } catch (DataAccessException e) {
            fail("DataAccessException should not be thrown");
        }
    }

    @Test
    @DisplayName("Clear Games - Positive")
    public void clearGamesPositive() throws DataAccessException {
        GameData game1 = dao.createGame("testGame1");
        GameData game2 = dao.createGame("testGame2");

        // Act: Clear the games
        dao.clearGame();

        // Assert: Verify the gameDataMap is empty and nextGameID is reset to 1
        Map<Integer, GameData> games = dao.listGames();
        assertTrue(games.isEmpty(), "The games list should be empty after clearing.");

        // Use reflection to check the value of nextGameID
        try {
            Field nextGameIDField = GameDataDAO.class.getDeclaredField("nextGameID");
            nextGameIDField.setAccessible(true);
            int nextGameID = (int) nextGameIDField.get(dao);
            assertEquals(1, nextGameID, "The nextGameID should be reset to 1 after clearing the games.");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to access nextGameID field.");
        }
    }
}


