package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.JoinGameRequest;
import spark.Request;
import spark.Response;
import spark.Route;
import service.GameService;
import model.ErrorResponse;
import dataaccess.DataAccessException;
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
            String authToken = request.headers("Authorization").replace("Bearer ", "").trim();
            System.out.println("JGH authToken: " + authToken);
            if (authToken == null || authToken.isEmpty()) {
                response.status(401);
                return gson.toJson(new ErrorResponse("Error: unauthorized"));
            }

            JoinGameRequest joinGameRequest = gson.fromJson(request.body(), JoinGameRequest.class);
            System.out.println("JoinGame request body: " + gson.toJson(joinGameRequest));

            if (joinGameRequest.getColor() == null || joinGameRequest.getGameId() == 0) {
                response.status(400);
                return gson.toJson(new ErrorResponse("Error: bad request"));
            }

            ChessGame.TeamColor playerColor;
            try {
                playerColor = ChessGame.TeamColor.valueOf(joinGameRequest.getColor());
            } catch (IllegalArgumentException e) {
                response.status(400);
                return gson.toJson(new ErrorResponse("Error: bad request"));
            }

            int gameId = joinGameRequest.getGameId();

            boolean joinSuccessful = gameService.joinGame(playerColor, gameId, authToken);

            if (joinSuccessful) {
                response.status(200);
                return "{}";
            } else {
                response.status(403);
                return gson.toJson(new ErrorResponse("Error: already taken"));
            }

        } catch (DataAccessException e) {
            response.status(401);
            return gson.toJson(new ErrorResponse("Error: unauthorized"));
        } catch (Exception e) {
            response.status(500);
            return gson.toJson(new ErrorResponse("Error: internal server error"));
        }
    }
}