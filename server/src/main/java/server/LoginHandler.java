package server;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;
import service.UserService;
import model.AuthData;
import model.UserData;
import dataaccess.DataAccessException;
import model.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginHandler implements Route {
    private final UserService userService;
    private final Gson gson = new Gson();
    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
            logger.info("Received login request: {}", request.body());
            UserData userData = gson.fromJson(request.body(), UserData.class);
            AuthData authData = userService.login(userData);
            response.status(200);
            logger.info("Login successful for user: {}", userData.username());
            return gson.toJson(authData);
        } catch (DataAccessException e) {
            logger.error("Login failed: {}", e.getMessage());
            response.status(401);
            return gson.toJson(new Error("Error: unauthorized"));
        } catch (Exception e) {
            logger.error("Internal server error during login: ", e);
            response.status(500);
            return gson.toJson(new Error("Error: internal server error"));
        }
    }
}
