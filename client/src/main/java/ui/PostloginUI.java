package ui;

import java.util.List;
import java.util.Scanner;
import model.GameData;  // Import the GameData class
import ui.PreloginUI;

public class PostloginUI {
    private ui.ServerFacade serverFacade;
    private Scanner scanner;
    private String authToken;
    private String username;
    private List<GameData> gameList;

    public PostloginUI(ui.ServerFacade serverFacade, Scanner scanner, String authToken, String username) {
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
                default:
                    System.out.println("Invalid command. Type 'help' to see the list of available commands.");
            }
        }
    }

    private void showHelp() {
        System.out.println("create <NAME> - a game");
        System.out.println("list - games");
        System.out.println("join <ID> [WHITE|BLACK] - a game");
        System.out.println("observe <ID> - a game");
        System.out.println("logout - when you are done");
        System.out.println("quit - playing chess");
        System.out.println("help - with possible commands");
    }

    private void logout() {
        serverFacade.logout(authToken);
        authToken = null;
        PreloginUI preloginUI = new PreloginUI(serverFacade, scanner);
        preloginUI.show();
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
            for (int i = 0; i < gameList.size(); i++) {
                GameData game = gameList.get(i);
                System.out.printf("%d. %s (Players: %s)\n", i + 1, game.getGameName(), game.getWhiteUsername(), game.getBlackUsername());
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
    }
}

