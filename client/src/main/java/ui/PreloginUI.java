package ui;

import java.util.Arrays;
import java.util.Scanner;

import com.google.gson.Gson;
import model.ResponseException;

public class PreloginUI {
    private String username = null;
    private final ServerFacade server;
    private final Scanner scanner;
    private State state = State.SIGNEDOUT;
    private String authToken;

    public PreloginUI(ServerFacade server, Scanner scanner) {
        this.server = server;
        this.scanner = scanner;
    }

    public String eval(String input) {
        try {
            var tokens = input.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help" -> help();
                case "quit" -> {
                    System.exit(0);
                    yield "quit";
                }
                case "login" -> login(params);
                case "register" -> register(params);
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "logout" -> logout();
                default -> "Invalid command. Type 'help' to see the list of available commands.";
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            String username = params[0];
            String password = params[1];
            authToken = server.login(username, password);
            if (authToken != null) {
                state = State.SIGNEDIN;
                this.username = username;
                PostloginUI postloginUI = new PostloginUI(server, scanner, authToken, username);
                postloginUI.show();
                return "Login successful.";
            } else {
                return "Login failed.";
            }
        }
        throw new ResponseException(400, "Expected: login <USERNAME> <PASSWORD>");
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            authToken = server.register(username, password, email);
            if (authToken != null) {
                state = State.SIGNEDIN;
                this.username = username;
                PostloginUI postloginUI = new PostloginUI(server, scanner, authToken, username);
                postloginUI.show();
                return "Registration successful.";
            } else {
                return "Registration failed.";
            }
        }
        throw new ResponseException(400, "Expected: register <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            String gameName = params[0];
            server.createGame(authToken, gameName);
            return String.format("Game '%s' created successfully.", gameName);
        }
        throw new ResponseException(400, "Expected: create <GAMENAME>");
    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        var games = server.listGames(authToken);
        var result = new StringBuilder();
        var gson = new Gson();
        for (var game : games.values()) {
            result.append(gson.toJson(game)).append('\n');
        }
        return result.toString();
    }

    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 2) {
            int gameId;
            try {
                gameId = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                throw new ResponseException(400, "Expected: join <GAMEID> <COLOR>");
            }
            String color = params[1];
            server.joinGame(authToken, gameId, color);
            return String.format("Joined game %d as %s", gameId, color);
        }
        throw new ResponseException(400, "Expected: join <GAMEID> <COLOR>");
    }

    public String logout() throws ResponseException {
        assertSignedIn();
        server.logout(authToken);
        state = State.SIGNEDOUT;
        authToken = null;
        return "Logout successful.";
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - register <USERNAME> <PASSWORD> <EMAIL> -to create an account
                    - login <USERNAME> <PASSWORD> - to play chess
                    - quit - playing chess
                    - help - with possible commands
                    """;
        }
        return """
                - create <GAMENAME>
                - list
                - join <GAMEID> <COLOR>
                - logout
                - quit
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}