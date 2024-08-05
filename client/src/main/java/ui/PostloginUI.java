package ui;

import java.util.Map;
import java.util.Scanner;
import model.GameData;
import java.util.Arrays;

public class PostloginUI {
    private final ServerFacade serverFacade;
    private final Scanner scanner;
    private final String authToken;
    private final String username;
    private Map<Integer, GameData> gameList;

    public PostloginUI(ServerFacade serverFacade, Scanner scanner, String authToken, String username) {
        this.serverFacade = serverFacade;
        this.scanner = scanner;
        this.authToken = authToken;
        this.username = username;
    }

    public void show() {
        System.out.println("Logged in as " + username);
        var result = "";
        while (!result.equals("quit")) {
            System.out.print("[LOGGED_IN] >>> ");
            String input = scanner.nextLine().trim().toLowerCase();
            result = eval(input);
            System.out.println(result);
        }
    }

    public String eval(String input) {
        try {
            var tokens = input.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help" -> showHelp();
                case "logout" -> {
                    logout();
                    yield "Logout successful.";
                }
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "play" -> playGame();
                case "quit" -> {
                    System.exit(0);
                    yield "quit";
                }
                default -> "Invalid command. Type 'help' to see the list of available commands.";
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String showHelp() {
        return """
                - create <NAME> - create a game
                - list - list games
                - join <ID> [WHITE|BLACK] - join a game
                - observe <ID> - observe a game
                - play - start playing the game
                - logout - log out
                - quit - exit the application
                - help - display help
                """;
    }

    private void logout() {
        serverFacade.logout(authToken);
        PreloginUI preloginUI = new PreloginUI(serverFacade, scanner);
        var result = "";
        while (!result.equals("quit")) {
            System.out.print("[LOGGED_OUT] >>> ");
            String input = scanner.nextLine().trim().toLowerCase();
            result = preloginUI.eval(input);
            System.out.println(result);
        }
    }

    private String createGame(String... params) {
        if (params.length == 1) {
            String gameName = params[0];
            serverFacade.createGame(authToken, gameName);
            return String.format("Game '%s' created successfully.", gameName);
        }
        return "Expected: create <NAME>";
    }

    private String listGames() {
        gameList = serverFacade.listGames(authToken);
        if (gameList != null) {
            var result = new StringBuilder();
            result.append("Available games:\n");
            for (Map.Entry<Integer, GameData> entry : gameList.entrySet()) {
                GameData game = entry.getValue();
                result.append(String.format("%d. %s (White: %s, Black: %s)\n", entry.getKey(), game.getGameName(),
                        game.getWhiteUsername(), game.getBlackUsername()));
            }
            return result.toString();
        }
        return "Failed to list games.";
    }

    private String joinGame(String... params) {
        if (gameList == null) {
            return "No games available. Use 'list' to see available games.";
        }
        if (params.length == 2) {
            int gameId;
            try {
                gameId = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                return "Expected: join <ID> [WHITE|BLACK]";
            }
            String color = params[1].toUpperCase();
            serverFacade.joinGame(authToken, gameId, color);
            return String.format("Joined game %d as %s", gameId, color);
        }
        return "Expected: join <ID> [WHITE|BLACK]";
    }

    private String observeGame(String... params) {
        if (gameList == null) {
            return "No games available. Use 'list' to see available games.";
        }
        if (params.length == 1) {
            int gameId;
            try {
                gameId = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                return "Expected: observe <ID>";
            }
            serverFacade.observeGame(authToken, gameId);
            GameplayUI.drawBoard();
            return String.format("Observing game %d", gameId);
        }
        return "Expected: observe <ID>";
    }

    private String playGame() {
        GameplayUI.drawBoard();
        return "Game started.";
    }
}