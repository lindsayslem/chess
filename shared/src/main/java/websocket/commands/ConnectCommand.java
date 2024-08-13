package websocket.commands;

public class ConnectCommand extends UserGameCommand {
    private String username;
    private String playerColor;
    private int gameID;

    // Constructor
    public ConnectCommand(String authToken, String username, String playerColor, int gameID) {
        super(authToken); // Assuming UserGameCommand has a constructor that accepts authToken
        this.username = username;
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    // Setter for playerName
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for playerName
    public String getUsername() {
        return this.username;
    }

    public String setPlayerColor(String playerColor) {
        return this.playerColor;
    }

    // Getter for side (player color)
    public String getPlayerColor() {
        return playerColor;
    }

    // Getter for gameId
    public int getGameID() {
        return this.gameID;
    }
}