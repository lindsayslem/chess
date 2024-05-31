package server;


import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;
import service.UserService;
import model.UserData;
import model.AuthData;

public class RegisterHandler implements Route {
    private final UserService userService;

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object handle(Request request, Response response) {
        Gson gson = new Gson();
        try {
            UserData userData = gson.fromJson(request.body(), UserData.class);
            AuthData authData = userService.register(userData);
            if (authData == null) {
                response.status(403);
                return gson.toJson(new Error("Error: already taken"));
            }
            response.status(200);
            return gson.toJson(authData);
        } catch (Exception e) {
            response.status(400);
            return gson.toJson(new Error("Error: bad request"));
        }
    }
}