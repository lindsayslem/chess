package dataaccess;

import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import chess.ChessGame;

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

    @Test
    @DisplayName("Create Game - Positive")
    public void createGamePositive() throws DataAccessException {
        GameData game = new GameData(1234, "whitePlayer", "blackPlayer", "testGame", new ChessGame());
        GameData createdGame = dao.createGame(game);

        assertNotNull(createdGame);
        assertEquals("testGame", createdGame.getGameName());
    }

    @Test
    @DisplayName("Create Game - Duplicate Game ID")
    public void createGameNegative() throws DataAccessException {
        GameData game1 = new GameData(1234, "whitePlayer1", "blackPlayer1", "testGame1", new ChessGame());
        dao.createGame(game1);

        GameData game2 = new GameData(1234, "whitePlayer2", "blackPlayer2","testGame1", new ChessGame());

        assertThrows(DataAccessException.class, () -> {
            dao.createGame(game2);
        });
    }

    @Test
    @DisplayName("Get Game - Positive")
    public void getGamePositive() throws DataAccessException {
        GameData game = new GameData(1234, "whitePlayer", "blackPlayer", "testGame", new ChessGame());
        GameData createdGame = dao.createGame(game);

        GameData retrievedGame = dao.getGame(createdGame.getGameID());

        assertNotNull(retrievedGame);
        assertEquals("testGame", retrievedGame.getGameName());
    }

    @Test
    @DisplayName("Get Game - Non-Existent ID")
    public void testGetGameNegative() throws DataAccessException {
        assertNull(dao.getGame(9999));
    }
}
