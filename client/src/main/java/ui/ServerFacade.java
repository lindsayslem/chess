package ui;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.CreateGameRequest;
import model.GameData;
import model.JoinGameRequest;

public class ServerFacade {
    private final String serverUrl;
    private final HttpClient client;
    private final Gson gson;

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
        this.client = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    public String register(String username, String password, String email) {
        try {
            String requestBody = String.format("{\"username\":\"%s\",\"password\":\"%s\",\"email\":\"%s\"}", username, password, email);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(serverUrl + "/user"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            System.out.println("Server URL: " + serverUrl + "/user");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status Line: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), JsonObject.class).get("authToken").getAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String login(String username, String password) {
        try {
            String requestBody = gson.toJson(new LoginRequest(username, password));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(serverUrl + "/session"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void logout(String authToken) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(serverUrl + "/session"))
                    .header("Authorization", "Bearer " + authToken)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createGame(String authToken, String gameName) {
        try {
            String requestBody = gson.toJson(new CreateGameRequest(gameName));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(serverUrl + "/game"))
                    .header("Authorization", "Bearer " + authToken.trim())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, GameData> listGames(String authToken) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(serverUrl + "/game"))
                    .header("Authorization", "Bearer " + authToken)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
                JsonArray gamesArray = jsonResponse.getAsJsonArray("games");
                Type gameListType = new TypeToken<List<GameData>>() {}.getType();
                List<GameData> gamesList = gson.fromJson(gamesArray, gameListType);

                // Convert the List<GameData> to Map<Integer, GameData>
                Map<Integer, GameData> gamesMap = new HashMap<>();
                for (GameData gameData : gamesList) {
                    gamesMap.put(gameData.getGameID(), gameData);
                }
                return gamesMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void joinGame(String authToken, int gameID, String playerColor) {
        try {
            String requestBody = gson.toJson(new JoinGameRequest(playerColor, gameID));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(serverUrl + "/game"))
                    .header("Authorization", "Bearer " + authToken)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void observeGame(String authToken, int gameId) {
        try {
            String requestBody = gson.toJson(new ObserveGameRequest(gameId));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(serverUrl + "/observe"))
                    .header("Authorization", "Bearer " + authToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class LoginRequest {
        String username;
        String password;

        LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    private static class ObserveGameRequest {
        int gameId;

        ObserveGameRequest(int gameId) {
            this.gameId = gameId;
        }
    }
}