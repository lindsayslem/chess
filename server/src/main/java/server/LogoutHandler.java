package server;

import spark.Request;
import spark.Response;
import spark.Route;
import service.UserService;
import dataAccess.DataAccessException;
import com.google.gson.Gson;
import model.Error;

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
            return gson.toJson(new Error("unauthorized"));
        }
        catch (Exception e) {
            response.status(500);
            return gson.toJson(new Error("internal server error"));        }
    }
}