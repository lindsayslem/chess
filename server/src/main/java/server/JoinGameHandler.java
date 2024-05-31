package server;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;
import service.GameService;
import model.GameData;
import dataaccess.DataAccessException;

public class JoinGameHandler implements Route {
    private final GameService gameService;

    public JoinGameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public Object handle(Request request, Response response) {
        Gson gson = new Gson();
        try {
            String authToken = request.headers("authorization");
            if (authToken == null || authToken.isEmpty()) {
                response.status(401);
                return gson.toJson(new Error("Error: unauthorized"));
            }

            GameData joinGameRequest = gson.fromJson(request.body(), GameData.class);
            boolean success = gameService.joinGame(joinGameRequest, authToken);
            if (!success) {
                response.status(400);
                return gson.toJson(new Error("Error: bad request"));
            }
            response.status(200);
            return "{}";
        } catch (DataAccessException e) {
            response.status(401);
            return gson.toJson(new Error("Error: unauthorized"));
        } catch (Exception e) {
            response.status(500);
            return gson.toJson(new Error("Error: internal server error"));
        }
    }
}