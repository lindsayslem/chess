package server;

import dataaccess.*;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.Spark;

public class Server {
    private boolean initialized = false;

    public int run(int desiredPort) throws DataAccessException {
        if (initialized) {
            Spark.stop();
            initialized = false;
        }

        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        // Initialize the DAOs
        MySqlUserDataDAO userDataDAO = new MySqlUserDataDAO();
        MySqlGameDataDAO gameDataDAO = new MySqlGameDataDAO();
        MySqlAuthDataDAO authDataDAO = new MySqlAuthDataDAO();

        // Initialize the service classes with the DAOs
        UserService userService = new UserService(userDataDAO, authDataDAO);
        GameService gameService = new GameService(gameDataDAO, authDataDAO);
        ClearService clearService = new ClearService(userDataDAO, gameDataDAO, authDataDAO);

        // Register the endpoints with their respective handlers
        Spark.post("/user", new RegisterHandler(userService));
        Spark.delete("/db", new ClearHandler(clearService));
        Spark.post("/game", new CreateGameHandler(gameService));
        Spark.put("/game", new JoinGameHandler(gameService));
        Spark.get("/game", new ListGamesHandler(gameService));
        Spark.post("/session", new LoginHandler(userService));
        Spark.delete("/session", new LogoutHandler(userService));

        Spark.awaitInitialization();
        initialized = true;
        return Spark.port();
    }

    public void stop() {
        if (initialized) {
            Spark.stop();
            Spark.awaitStop(); // Ensure Spark is fully stopped
            initialized = false;
        }
    }
}
