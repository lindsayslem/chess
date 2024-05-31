package server;

import spark.Request;
import spark.Response;
import spark.Route;
import service.ClearService;
import dataaccess.DataAccessException;

public class ClearHandler implements Route {
    private final ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
            clearService.clear();
            response.status(200);
            return "{}";
        } catch (DataAccessException e) {
            response.status(500);
            return "{\"message\": \"Error: internal server error\"}";
        }
    }
}