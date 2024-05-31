package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import spark.Route;
import service.UserService;
import model.UserData;
import model.AuthData;

public class LoginHandler implements Route {
    private final UserService userService;

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object handle(Request request, Response response) {
        Gson gson = new Gson();
        try {
            UserData loginRequest = gson.fromJson(request.body(), UserData.class);
            AuthData authData = userService.login(loginRequest);
            if (authData == null) {
                response.status(401);
                return gson.toJson(new Error("Error: unauthorized"));
            }
            response.status(200);
            return gson.toJson(authData);
        } catch (DataAccessException e) {
            response.status(500);
            return gson.toJson(new Error("Error: internal server error"));
        }
    }
}