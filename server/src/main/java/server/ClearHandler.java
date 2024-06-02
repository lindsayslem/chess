package server;

import spark.Request;
import spark.Response;
import spark.Route;
import service.ClearService;
import dataAccess.DataAccessException;
import com.google.gson.Gson;
import model.Error;

public class ClearHandler implements Route {
    private final ClearService clearService;
    private final Gson gson = new Gson();

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    @Override
    public Object handle(Request request, Response response) {
        try {
            clearService.clear();
            response.status(200);
            return gson.toJson(new Object());
        } catch (DataAccessException e) {
            response.status(500);
            return gson.toJson(new Error("Error"));
        }
        catch (Exception e) {
            response.status(500);
            return gson.toJson(new Error("Error: internal server error"));
    }
    }
}