package server;

import dataaccess.*;
import model.GameData;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.Spark;

import java.util.Map;

public class Server {
    private boolean initialized = false;
    UserService userService;
    GameService gameService;
    ClearService clearService;
    private MySqlGameDataDAO gameDataDAO;

    public Server() {
        try{
            DatabaseSetup.configureDatabase();
        } catch(DataAccessException e){
            e.printStackTrace();
        }
        MySqlUserDataDAO userDataDAO = new MySqlUserDataDAO();
        gameDataDAO = new MySqlGameDataDAO();
        MySqlAuthDataDAO authDataDAO = new MySqlAuthDataDAO();

        userService = new UserService(userDataDAO, authDataDAO);
        gameService = new GameService(gameDataDAO, authDataDAO);
        clearService = new ClearService(userDataDAO, gameDataDAO, authDataDAO);
    }

    public int run(int desiredPort) {
        if (initialized) {
            Spark.stop();
            initialized = false;
        }

        Spark.port(desiredPort);
        Spark.staticFiles.location("/web");

        Spark.webSocket("/ws", WebSocketHandler.class);


        Spark.post("/user", new RegisterHandler(userService));
        Spark.delete("/db", new ClearHandler(clearService));
        Spark.post("/game", new CreateGameHandler(gameService));
        Spark.put("/game", new JoinGameHandler(gameService));
        Spark.get("/game", new ListGamesHandler(gameService));
        Spark.post("/session", new LoginHandler(userService));
        Spark.delete("/session", new LogoutHandler(userService));

        loadGameData();

        Spark.awaitInitialization();
        initialized = true;
        return Spark.port();
    }

    public void stop() {
        if (initialized) {
            Spark.stop();
            Spark.awaitStop();
            initialized = false;
        }
    }

    private void loadGameData() {
        try {
            Map<Integer, GameData> games = gameDataDAO.listGames();
            for (GameData game : games.values()) {
                System.out.println("Loaded game: " + game.getGameName());
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}