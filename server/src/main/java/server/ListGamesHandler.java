package server;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;
import service.GameService;
import model.GameData;
import dataAccess.DataAccessException;
import java.util.List;



public class ListGamesHandler implements Route {
    private final GameService gameService;
    private final Gson gson = new Gson();

    public ListGamesHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
            String authToken = request.headers("authorization");
            List<GameData> games = gameService.listGames(authToken);
            response.status(200);
            return gson.toJson(games);
        } catch (DataAccessException e) {
            response.status(401);
            return gson.toJson(new Error("Error: unauthorized"));
        } catch (Exception e) {
            response.status(500);
            return gson.toJson(new Error("Error: internal server error"));
        }
    }
}