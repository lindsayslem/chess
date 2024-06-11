package ui;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import model.GameData;

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
            String requestBody = gson.toJson(new RegisterRequest(username, password, email));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(serverUrl + "/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();  // Assuming the response body contains the auth token
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
                    .uri(new URI(serverUrl + "/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();  // Assuming the response body contains the auth token
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void logout(String authToken) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(serverUrl + "/logout"))
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
                    .uri(new URI(serverUrl + "/games"))
                    .header("Authorization", "Bearer " + authToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<GameData> listGames(String authToken) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(serverUrl + "/games"))
                    .header("Authorization", "Bearer " + authToken)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Type gameListType = new TypeToken<ArrayList<GameData>>() {}.getType();
                return gson.fromJson(response.body(), gameListType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void joinGame(String authToken, int gameId, String color) {
        try {
            String requestBody = gson.toJson(new JoinGameRequest(gameId, color));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(serverUrl + "/games/join"))
                    .header("Authorization", "Bearer " + authToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
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
                    .uri(new URI(serverUrl + "/games/observe"))
                    .header("Authorization", "Bearer " + authToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper classes for request bodies
    private static class RegisterRequest {
        String username;
        String password;
        String email;

        RegisterRequest(String username, String password, String email) {
            this.username = username;
            this.password = password;
            this.email = email;
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

    private static class CreateGameRequest {
        String name;

        CreateGameRequest(String name) {
            this.name = name;
        }
    }

    private static class JoinGameRequest {
        int gameId;
        String color;

        JoinGameRequest(int gameId, String color) {
            this.gameId = gameId;
            this.color = color;
        }
    }

    private static class ObserveGameRequest {
        int gameId;

        ObserveGameRequest(int gameId) {
            this.gameId = gameId;
        }
    }
}
