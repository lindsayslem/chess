package server;


import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;
import service.UserService;
import model.UserData;
import model.AuthData;
import dataAccess.DataAccessException;
import model.Error;


public class RegisterHandler implements Route {
    private final UserService userService;
    private final Gson gson = new Gson();

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
            UserData userData = gson.fromJson(request.body(), UserData.class);
            if(userData.getUsername() == null || userData.getPassword() == null || userData.getEmail() == null){
                response.status(400);
                return gson.toJson(new Error("missing field"));
            }
            AuthData authData = userService.register(userData);
            response.status(200);
            return gson.toJson(authData);
        }
        catch(DataAccessException e){
            response.status(403);
            return gson.toJson(new Error("Error: already taken"));
        } catch(Exception e) {
            response.status(400);
            return gson.toJson(new Error("Error: bad request"));
        }
    }
}