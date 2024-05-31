package server;



import spark.Request;
import spark.Response;
import spark.Route;
import service.UserService;

public class LogoutHandler implements Route {
    private final UserService userService;

    public LogoutHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
            String authToken = request.headers("authorization");
            if (authToken == null || authToken.isEmpty()) {
                response.status(401);
                return "{\"message\": \"Error: unauthorized\"}";
            }

            boolean success = userService.logout(authToken);
            if (!success) {
                response.status(401);
                return "{\"message\": \"Error: unauthorized\"}";
            }
            response.status(200);
            return "{}";
        } catch (Exception e) {
            response.status(500);
            return "{\"message\": \"Error: internal server error\"}";
        }
    }
}