package client;

import dataaccess.*;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;
import model.GameData;
import service.ClearService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static ClearService clearService;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade("http://localhost:" + port);
        clearService = new ClearService(new MySqlUserDataDAO(), new MySqlGameDataDAO(), new MySqlAuthDataDAO());
    }

    @AfterAll
    public static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clearDatabase() throws Exception {
        clearService.clear();
    }


    @Test
    void registerPositive() {
        String authToken = facade.register("player1", "password", "p1@email.com");
        assertNotNull(authToken);
        assertTrue(authToken.length() > 10);
    }

    @Test
    void registerNegative(){
        String authToken = facade.register("player1", "", "p1@email.com");
        assertNull(authToken);
    }

    @Test
    void loginPositive() {
        facade.register("player1", "password", "p1@email.com");
        String authToken = facade.login("player1", "password");
        assertNotNull(authToken);
        assertTrue(authToken.length() > 10);
    }

    @Test
    void loginNegative() {
        String authToken = facade.login("nonexistent", "wrongpassword");
        assertNull(authToken);
    }

    @Test
    public void createGamePositive() {
        String authToken = facade.register("player1", "password", "p1@email.com");
        System.out.println("Auth Token after registration: " + authToken);
        assertNotNull(authToken);

        facade.createGame(authToken, "NewGame");
        Map<Integer, GameData> games = facade.listGames(authToken);
        System.out.println("List of Games: " + games);
        assertNotNull(games);
        assertEquals(1, games.size());
        assertEquals("NewGame", games.values().iterator().next().getGameName());
    }


    @Test
    void createGameNegative() {
        // Attempt to create a game without a valid auth token
        facade.createGame("invalidToken", "NewGame");
        Map<Integer, GameData> games = facade.listGames("invalidToken");
        assertNull(games);
    }

    @Test
    void listGamesPositive() {
        String authToken = facade.register("player1", "password", "p1@email.com");
        assertNotNull(authToken);

        facade.createGame(authToken, "NewGame");
        Map<Integer, GameData> games = facade.listGames(authToken);
        assertNotNull(games);
        assertEquals(1, games.size());
    }

    @Test
    void listGamesNegative() {
        // Attempt to list games without a valid auth token
        Map<Integer, GameData> games = facade.listGames("invalidToken");
        assertNull(games);
    }

    @Test
    void joinGamePositive() {
        String authToken = facade.register("player1", "password", "p1@email.com");
        assertNotNull(authToken);

        facade.createGame(authToken, "NewGame");
        Map<Integer, GameData> games = facade.listGames(authToken);
        int gameId = games.keySet().iterator().next();

        facade.joinGame(authToken, gameId, "WHITE");
        games = facade.listGames(authToken);
        assertEquals("player1", games.get(gameId).getWhiteUsername());
    }

    @Test
    void joinGameNegative() {
        // Attempt to join a game without a valid auth token
        facade.joinGame("invalidToken", 1, "WHITE");
        Map<Integer, GameData> games = facade.listGames("invalidToken");
        assertNull(games);
    }

    @Test
    void observeGamePositive() {
        String authToken = facade.register("player1", "password", "p1@email.com");
        assertNotNull(authToken);

        facade.createGame(authToken, "NewGame");
        Map<Integer, GameData> games = facade.listGames(authToken);
        int gameId = games.keySet().iterator().next();

        facade.observeGame(authToken, gameId);
        // Verify that the game observation started successfully
    }

    @Test
    void observeGameNegative() {
        // Attempt to observe a game without a valid auth token
        facade.observeGame("invalidToken", 1);
        Map<Integer, GameData> games = facade.listGames("invalidToken");
        assertNull(games);
    }
}
