package ui;

import java.util.Scanner;

public class ChessRepl {
    private final PreloginUI client;

    public ChessRepl(String serverUrl) {
        ServerFacade serverFacade = new ServerFacade(serverUrl);
        Scanner scanner = new Scanner(System.in);
        client = new PreloginUI(serverFacade, scanner);
    }

    public void run() {
        System.out.println("♕ Welcome to 240 chess. Sign in to start. Type Help to get started, ♕");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n>>> ");
    }
}