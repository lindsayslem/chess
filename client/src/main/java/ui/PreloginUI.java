package ui;

import java.util.Scanner;
import service.ClearService;
import dataaccess.*;

public class PreloginUI {
    private ServerFacade serverFacade;
    private ClearService clearService;
    private Scanner scanner;

    public PreloginUI(ServerFacade serverFacade, Scanner scanner) {
        this.serverFacade = serverFacade;
        this.scanner = scanner;

        // Initialize ClearService
        IUserDataDAO userDataDAO = new MySqlUserDataDAO();
        IGameDataDAO gameDataDAO = new MySqlGameDataDAO();
        IAuthDataDAO authDataDAO = new MySqlAuthDataDAO();
        this.clearService = new ClearService(userDataDAO, gameDataDAO, authDataDAO);
    }

    public void show(){
        System.out.println("♕ Welcome to 240 chess. Type Help to get started. ♕");
        while(true){
            System.out.print("[LOGGED_OUT] >>> ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command){
                case "help":
                    showHelp();
                    break;
                case "quit":
                    System.exit(0);
                case "login":
                    login();
                    break;
                case "register":
                    register();
                    break;
                case "cleardb":  // Example command to clear the database
                    clearDatabase();
                    break;
                default:
                    System.out.println("Invalid command. Type 'help' to see the list of available commands.");
            }
        }
    }

    private void showHelp(){
        System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
        System.out.println("login <USERNAME> <PASSWORD> - to play chess");
        System.out.println("cleardb - clear the database");
        System.out.println("quit - exit the application");
        System.out.println("help - with possible commands");
    }

    private void login(){
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        String authToken = serverFacade.login(username, password);
        if(authToken != null){
            PostloginUI postloginUI = new PostloginUI(serverFacade, scanner, authToken, username);
            postloginUI.show();
        }
        else{
            System.out.println("Login failed.");
        }
    }

    private void register(){
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        String authToken = null;
        authToken = serverFacade.register(username, password, email);
        if(authToken != null){
            PostloginUI postloginUI = new PostloginUI(serverFacade, scanner, authToken, username);
            postloginUI.show();
        }
        else{
            System.out.println("Registration failed.");
        }
    }

    private void clearDatabase() {
        try {
            clearService.clear();
            System.out.println("Database cleared.");
        } catch (DataAccessException e) {
            System.out.println("Failed to clear database: " + e.getMessage());
        }
    }
}
