package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import spark.Request;
import spark.Response;
import spark.Route;
import service.GameService;
import model.Error;
import dataAccess.DataAccessException;
import chess.ChessGame;

public class JoinGameHandler implements Route {
    private final GameService gameService;
    private final Gson gson = new Gson();

    public JoinGameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
            String authToken = request.headers("authorization");

            if (authToken == null || authToken.isEmpty()) {
                response.status(401);
                return gson.toJson(new Error("Error: unauthorized"));
            }

            JsonObject jsonObject = JsonParser.parseString(request.body()).getAsJsonObject();

            if (!jsonObject.has("playerColor") || !jsonObject.has("gameID")) {
                response.status(400);
                return gson.toJson(new Error("Error: bad request"));
            }

            String playerColorStr = jsonObject.get("playerColor").getAsString();
            ChessGame.TeamColor playerColor;
            try {
                playerColor = ChessGame.TeamColor.valueOf(playerColorStr);
            } catch (IllegalArgumentException e) {
                response.status(400);
                return gson.toJson(new Error("Error: bad request"));
            }

            int gameID = jsonObject.get("gameID").getAsInt();

            boolean joinSuccessful = gameService.joinGame(playerColor, gameID, authToken);

            if (joinSuccessful) {
                response.status(200);
                return "{}";
            } else {
                response.status(403);
                return gson.toJson(new Error("Error: already taken"));
            }

        } catch (DataAccessException e) {
            response.status(401);
            return gson.toJson(new Error("Error: unauthorized"));
        } catch (Exception e) {
            response.status(500);
            return gson.toJson(new Error("Error: internal server error"));
        }
    }
}
