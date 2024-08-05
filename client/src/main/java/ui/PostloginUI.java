package ui;

import java.util.Map;
import java.util.Scanner;

import model.GameData;

public class PostloginUI {
    private final ServerFacade serverFacade;
    private final Scanner scanner;
    private String authToken;
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
        while (true) {
            System.out.print("[LOGGED_IN] >>> ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "help":
                    showHelp();
                    break;
                case "logout":
                    logout();
                    return;
                case "create":
                    createGame();
                    break;
                case "list":
                    listGames();
                    break;
                case "join":
                    joinGame();
                    break;
                case "observe":
                    observeGame();
                    break;
                case "quit":
                    System.exit(0);
                case "play":
                    playGame();
                    break;
                default:
                    System.out.println("Invalid command. Type 'help' to see the list of available commands.");
            }
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
        authToken = null;
        State state = State.SIGNEDOUT;

        PreloginUI preloginUI = new PreloginUI(serverFacade, scanner);
        String result = "";
        while (!result.equals("quit")) {
            System.out.print("[LOGGED_OUT] >>> ");
            String line = scanner.nextLine();
            result = preloginUI.eval(line);
            System.out.print(result);
        }
    }

    private void createGame() {
        System.out.print("Enter game name: ");
        String gameName = scanner.nextLine();
        serverFacade.createGame(authToken, gameName);
    }

    private void listGames() {
        gameList = serverFacade.listGames(authToken);
        if (gameList != null) {
            System.out.println("Available games:");
            for (Map.Entry<Integer, GameData> entry : gameList.entrySet()) {
                GameData game = entry.getValue();
                System.out.printf("%d. %s (White: %s, Black: %s)\n", entry.getKey(), game.getGameName(),
                        game.getWhiteUsername(), game.getBlackUsername());
            }
        } else {
            System.out.println("Failed to list games.");
        }
    }

    private void joinGame() {
        if (gameList == null) {
            System.out.println("No games available. Use 'list' to see available games.");
            return;
        }
        System.out.print("Enter game ID to join: ");
        int gameId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter color (WHITE/BLACK): ");
        String color = scanner.nextLine().toUpperCase();
        serverFacade.joinGame(authToken, gameId, color);
    }

    private void observeGame() {
        if (gameList == null) {
            System.out.println("No games available. Use 'list' to see available games.");
            return;
        }
        System.out.print("Enter game ID to observe: ");
        int gameId = Integer.parseInt(scanner.nextLine());
        serverFacade.observeGame(authToken, gameId);
        GameplayUI.drawBoard();
    }

    private void playGame() {
        GameplayUI.drawBoard();
    }
}