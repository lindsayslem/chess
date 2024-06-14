package client;

import dataaccess.*;
import org.junit.jupiter.api.*;
import server.Server;
import service.ClearService;
import ui.ServerFacade;
import model.AuthData;
import model.GameData;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    public static void stopServer() {
        server.stop();
    }

   /*@BeforeEach
    public void setUp() throws DataAccessException {
        IUserDataDAO userDataDAO = new MySqlUserDataDAO();
        IGameDataDAO gameDataDAO = new MySqlGameDataDAO();
        IAuthDataDAO authDataDAO = new MySqlAuthDataDAO();
        ClearService clearService = new ClearService(userDataDAO, gameDataDAO, authDataDAO);
        clearService.clear();
    }*/
    @Test
    void registerSuccess() throws Exception {
        String authData = facade.register("player1", "password", "p1@email.com");
        assertNotNull(authData);
        assertTrue(authData.length() > 10);
    }

    @Test
    void registerFailure() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        String authData = facade.register("player1", "password", "p1@email.com"); // Register with the same username
        assertNull(authData);
    }

    @Test
    void loginSuccess() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        String authData = facade.login("player1", "password");
        assertNotNull(authData);
        assertTrue(authData.length() > 10);
    }

    @Test
    void loginFailure() throws Exception {
        String authData = facade.login("player1", "wrongpassword");
        assertNull(authData);
    }

    @Test
    void createGameSuccess() throws Exception {
        String authToken = facade.register("player1", "password", "p1@email.com");
        assertNotNull(authToken);

        facade.createGame(authToken, "NewGame");
        Map<Integer, GameData> games = facade.listGames(authToken);
        assertNotNull(games);
        assertEquals(1, games.size());
        assertEquals("NewGame", games.values().iterator().next().getGameName());
    }

    @Test
    void createGameFailure() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        facade.createGame("invalidAuthToken", "NewGame");
        Map<Integer, GameData> games = facade.listGames("invalidAuthToken");
        assertNull(games);
    }

    @Test
    void listGamesSuccess() throws Exception {
        String authToken = facade.register("player1", "password", "p1@email.com");
        facade.createGame(authToken, "NewGame1");
        facade.createGame(authToken, "NewGame2");
        Map<Integer, GameData> games = facade.listGames(authToken);
        assertEquals(2, games.size());
    }

    @Test
    void listGamesFailure() throws Exception {
        Map<Integer, GameData> games = facade.listGames("invalidAuthToken");
        assertNull(games);
    }

    @Test
    void joinGameSuccess() throws Exception {
        String authToken = facade.register("player1", "password", "p1@email.com");
        facade.createGame(authToken, "NewGame");
        Map<Integer, GameData> games = facade.listGames(authToken);
        facade.joinGame(authToken, games.get(0).getGameID(), "WHITE");
        games = facade.listGames(authToken);
        assertEquals("player1", games.get(0).getWhiteUsername());
    }

    @Test
    void joinGameFailure() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        facade.joinGame("invalidAuthToken", 1, "WHITE");
        // Ensure join game failure does not affect game list
        Map<Integer, GameData> games = facade.listGames("invalidAuthToken");
        assertNull(games);
    }

    @Test
    void observeGameSuccess() throws Exception {
        String authToken = facade.register("player1", "password", "p1@email.com");
        facade.createGame(authToken, "NewGame");
        Map<Integer, GameData> games = facade.listGames(authToken);
        facade.observeGame(authToken, games.get(0).getGameID());
        // No specific observable check here, but ensure no exception and game exists
        games = facade.listGames(authToken);
        assertEquals(1, games.size());
    }

    @Test
    void observeGameFailure() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        facade.observeGame("invalidAuthToken", 1);
        // Ensure observe game failure does not affect game list
        Map<Integer, GameData> games = facade.listGames("invalidAuthToken");
        assertNull(games);
    }
}
