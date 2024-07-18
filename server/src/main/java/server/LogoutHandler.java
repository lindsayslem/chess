package server;

import spark.Request;
import spark.Response;
import spark.Route;
import service.UserService;
import dataaccess.DataAccessException;
import com.google.gson.Gson;
import model.ErrorResponse;

public class LogoutHandler implements Route {
    private final UserService userService;
    private final Gson gson = new Gson();

    public LogoutHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
            String authToken = request.headers("authorization");
            userService.logout(authToken);
            response.status(200);
            return "{}";
        }
        catch (DataAccessException e){
            response.status(401);
            return gson.toJson(new ErrorResponse("Error: unauthorized"));
        }
        catch (Exception e) {
            response.status(500);
            return gson.toJson(new ErrorResponse("Error: internal server error"));        }
    }
}