package server;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;
import service.GameService;
import model.GameData;
import dataaccess.DataAccessException;
import java.util.Map;
import model.ErrorResponse;
public class ListGamesHandler implements Route {
    private final GameService gameService;
    private final Gson gson = new Gson();
    public ListGamesHandler(GameService gameService) {
        this.gameService = gameService;
    }
    @Override
    public Object handle(Request request, Response response) {
        try {
            String authToken = request.headers("Authorization").replace("Bearer ", "");
            // Check if the auth token is present and valid
            if (authToken == null || authToken.isEmpty()) {
                response.status(401);
                return gson.toJson(new ErrorResponse("Error: unauthorized"));
            }
            Map<Integer, GameData> games = gameService.listGames(authToken);
            response.status(200);
            return gson.toJson(games);
        }  catch (DataAccessException e) {
            response.status(401);
            return gson.toJson(new ErrorResponse("Error: unauthorized"));
        } catch (Exception e) {
            response.status(500);
            return gson.toJson(new ErrorResponse("Error: internal server error"));
        }
    }
}