package server;

import spark.Spark;
import service.ClearService;
import service.GameService;
import service.UserService;
import dataAccess.AuthDataDAO;
import dataAccess.GameDataDAO;
import dataAccess.UserDataDAO;


public class Server {
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Initialize the DAOs
        UserDataDAO userDataDAO = new UserDataDAO();
        GameDataDAO gameDataDAO = new GameDataDAO();
        AuthDataDAO authDataDAO = new AuthDataDAO();

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
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
    }
}
