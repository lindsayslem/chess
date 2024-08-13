package ui;

import com.google.gson.Gson;
import model.ResponseException;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.ResignCommand;

import java.io.IOException;
import java.util.Map;

public class GameplayUI {
    private WebSocketFacade webSocketClient;
    private String username;
    private String authToken;
    private int gameID;

    private static final String[] PIECES = {
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.WHITE_ROOK, EscapeSequences.WHITE_KNIGHT, EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_KING, EscapeSequences.WHITE_QUEEN,
            EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_KNIGHT, EscapeSequences.WHITE_ROOK,
            EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN,
            EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN,
            EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN,
            EscapeSequences.BLACK_ROOK, EscapeSequences.BLACK_KNIGHT, EscapeSequences.BLACK_BISHOP,
            EscapeSequences.BLACK_KING, EscapeSequences.BLACK_QUEEN, EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_KNIGHT, EscapeSequences.BLACK_ROOK,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY
    };

    private static final String[] PIECES_FLIPPED = {
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.BLACK_ROOK, EscapeSequences.BLACK_KNIGHT, EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_QUEEN,
            EscapeSequences.BLACK_KING, EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_KNIGHT, EscapeSequences.BLACK_ROOK,
            EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN,
            EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN,
            EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN,
            EscapeSequences.WHITE_ROOK, EscapeSequences.WHITE_KNIGHT, EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_QUEEN,
            EscapeSequences.WHITE_KING, EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_KNIGHT, EscapeSequences.WHITE_ROOK,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY
    };

    public GameplayUI(String serverUri, String authToken, String username, String side, int gameID) {
        this.authToken = authToken;
        this.username = username;
        this.gameID = gameID;

        try {
            this.webSocketClient = new WebSocketFacade(serverUri, notification -> {
                // Handle incoming notifications here
                System.out.println("Received notification: " + notification.getMessage());
            });
            sendConnectCommand(username, side);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendConnectCommand(String username, String side) {
        try {
            System.out.println("Creating ConnectCommand with username: " + username);
            ConnectCommand command = new ConnectCommand(authToken, username, side, gameID);
            webSocketClient.sendCommand(command);
        } catch (IOException e) {
            System.err.println("Failed to send CONNECT command: " + e.getMessage());
        }
    }

    public void makeMove(String move) {
        try {
            MakeMoveCommand command = new MakeMoveCommand(authToken, username, move);
            webSocketClient.sendCommand(command);
        } catch (IOException e) {
            System.err.println("Failed to send MAKE_MOVE command: " + e.getMessage());
        }
    }

    public void leaveGame() {
        try {
            LeaveCommand command = new LeaveCommand(authToken, username);
            webSocketClient.sendCommand(command);
        } catch (IOException e) {
            System.err.println("Failed to send LEAVE command: " + e.getMessage());
        }
    }

    public void resignGame() {
        try {
            ResignCommand command = new ResignCommand(authToken, username);
            webSocketClient.sendCommand(command);
        } catch (IOException e) {
            System.err.println("Failed to send RESIGN command: " + e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            webSocketClient.closeConnection();
        } catch (IOException e) {
            System.err.println("Failed to close WebSocket connection: " + e.getMessage());
        }
    }

    public void drawBoard() {
        drawBoardWhiteTop();
        System.out.println("\n");
        drawBoardBlackTop();
    }

    private static void drawBoardWhiteTop() {
        String borderColor = EscapeSequences.SET_BG_COLOR_BLACK;
        String[] boardColors = { EscapeSequences.SET_BG_COLOR_LIGHT_GREY, EscapeSequences.SET_BG_COLOR_DARK_GREY };
        String columnLabels = " hgfedcba";

        boardSetup(GameplayUI.PIECES, borderColor, boardColors, columnLabels, "1", "2", "3", "4", "5", "6", "7", "8");
    }

    private static void drawBoardBlackTop() {
        String borderColor = EscapeSequences.SET_BG_COLOR_BLACK;
        String[] boardColors = { EscapeSequences.SET_BG_COLOR_LIGHT_GREY, EscapeSequences.SET_BG_COLOR_DARK_GREY };
        String columnLabels = " abcdefgh";
        boardSetup(GameplayUI.PIECES_FLIPPED, borderColor, boardColors, columnLabels, "8", "7", "6", "5",
                "4", "3", "2", "1");
    }

    private static void boardSetup(String[] pieces, String borderColor, String[] boardColors, String columnLabels,
                                   String number, String number2, String number3, String number4, String number5,
                                   String number6, String number7, String number8) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (row == 0 || row == 9) {
                    if (col == 0 || col == 9) {
                        System.out.print(borderColor + "   ");
                    } else {
                        System.out.print(borderColor + " " + columnLabels.charAt(col) + " ");
                    }
                } else if (col == 0 || col == 9) {
                    System.out.print(borderColor + " " + (row == 1 ? number : row == 2 ? number2 : row == 3 ? number3 :
                            row == 4 ? number4 : row == 5 ? number5 : row == 6 ? number6 : row == 7 ? number7 :
                                    row == 8 ? number8 : " ") + " ");
                } else {
                    int pieceIndex = (row - 1) * 8 + (col - 1);
                    String piece = pieces[pieceIndex];
                    System.out.print(boardColors[(row + col) % 2] + piece + EscapeSequences.RESET_TEXT_COLOR);
                }
            }
            System.out.print(EscapeSequences.RESET_BG_COLOR);
            System.out.println();
        }
    }

    public void updateBoardState(String gameStateJson) {
        Gson gson = new Gson();
        GameState gameState = gson.fromJson(gameStateJson, GameState.class);

        // Clear the board first
        for (int i = 0; i < 64; i++) {
            PIECES[i] = EscapeSequences.EMPTY;
            PIECES_FLIPPED[i] = EscapeSequences.EMPTY;
        }

        // Update the PIECES and PIECES_FLIPPED arrays based on the game state
        for (Map.Entry<String, String> entry : gameState.getPieces().entrySet()) {
            String position = entry.getKey();
            String pieceSymbol = entry.getValue();

            int index = convertPositionToIndex(position);
            int flippedIndex = 63 - index;

            PIECES[index] = pieceSymbol;
            PIECES_FLIPPED[flippedIndex] = pieceSymbol;
        }

        // Redraw the board after updating the state
        drawBoard();
    }

    private int convertPositionToIndex(String position) {
        char file = position.charAt(0);
        char rank = position.charAt(1);

        int fileIndex = file - 'a';
        int rankIndex = 8 - (rank - '1') - 1;

        return rankIndex * 8 + fileIndex;
    }

    public static void main(String[] args) {
        // Example usage with a valid auth token, player name, and game ID.
        GameplayUI gameplayUI = new GameplayUI("ws://localhost:8080/ws", "9063259e-c266-4b4e-84d2-cc18428f8793", "PlayerName", "WHITE", 5);
        gameplayUI.drawBoard();

        // Simulate receiving a game state from the server
        String gameStateJson = "{ \"pieces\": { \"e2\": \"♙\", \"e4\": \"♟\", \"h8\": \"♜\" } }";
        gameplayUI.updateBoardState(gameStateJson);

        // Make a move example
        gameplayUI.makeMove("e2e4");

        // Other commands
        gameplayUI.leaveGame();
        gameplayUI.resignGame();

        // Close connection when done
        gameplayUI.closeConnection();
    }
}

class GameState {
    private Map<String, String> pieces;

    public Map<String, String> getPieces() {
        return pieces;
    }

    public void setPieces(Map<String, String> pieces) {
        this.pieces = pieces;
    }
}