package server;

import chess.ChessGame;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;
import service.GameService;
import model.GameData;
import dataaccess.DataAccessException;

public class CreateGameHandler implements Route {
    private final GameService gameService;
    private final Gson gson = new Gson();

    public CreateGameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
            String authToken = request.headers("Authorization");
            GameData gameData = gson.fromJson(request.body(), GameData.class);
            GameData createdGame = gameService.createGame(gameData, authToken);
            response.status(200);
            return gson.toJson(createdGame);
        } catch (DataAccessException e) {
            response.status(401);
            return gson.toJson(new Error("Error: unauthorized"));
        } catch (Exception e) {
            response.status(500);
            return gson.toJson(new GameData(0, null, null, "Error: internal server error", null));
        }
    }
}
