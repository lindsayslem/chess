package server;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;
import service.UserService;
import model.AuthData;
import model.UserData;
import dataaccess.DataAccessException;
import model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginHandler implements Route {
    private final UserService userService;
    private final Gson gson = new Gson();
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginHandler.class);

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }
    @Override
    public Object handle(Request request, Response response) {
        try {
            LOGGER.info("Received login request: {}", request.body());
            UserData userData = gson.fromJson(request.body(), UserData.class);
            AuthData authData = userService.login(userData);
            response.status(200);
            LOGGER.info("Login successful for user: {}", userData.username());
            return gson.toJson(authData);
        } catch (DataAccessException e) {
            LOGGER.error("Login failed: {}", e.getMessage());
            response.status(401);
            return gson.toJson(new ErrorResponse("Error: unauthorized"));
        } catch (Exception e) {
            LOGGER.error("Internal server error during login: ", e);
            response.status(500);
            return gson.toJson(new ErrorResponse("Error: internal server error"));
        }
    }
}