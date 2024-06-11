package ui;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.GameData;

public class ServerFacade {
    private final String serverUrl;
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public String register(String username, String password, String email) {
        try {
            String requestBody = String.format("{\"username\":\"%s\",\"password\":\"%s\",\"email\":\"%s\"}", username, password, email);
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
            String requestBody = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
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
            String requestBody = String.format("{\"name\":\"%s\"}", gameName);
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
                return objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, GameData.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void joinGame(String authToken, int gameId, String color) {
        try {
            String requestBody = String.format("{\"gameId\":%d,\"color\":\"%s\"}", gameId, color);
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
            String requestBody = String.format("{\"gameId\":%d}", gameId);
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
}
