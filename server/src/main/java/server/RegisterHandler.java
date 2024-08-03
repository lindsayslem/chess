package server;


import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;
import service.UserService;
import model.UserData;
import model.AuthData;
import dataaccess.DataAccessException;
import model.ErrorResponse;


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
            if(userData.username() == null || userData.password() == null || userData.email() == null){
                response.status(400);
                return gson.toJson(new ErrorResponse("Error: bad request"));
            }
            AuthData authData = userService.register(userData);
            response.status(200);
            return gson.toJson(authData);
        }
        catch(DataAccessException e){
            response.status(403);
            return gson.toJson(new ErrorResponse("Error: already taken"));
        } catch(Exception e) {
            response.status(500);
            return gson.toJson(new ErrorResponse("Error: bad request"));
        }
    }
}