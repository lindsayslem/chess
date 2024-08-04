package model;

public class JoinGameRequest {
    private final int gameId;
    private final String color;

    public JoinGameRequest(int gameId, String color) {
        this.gameId = gameId;
        this.color = color;
    }

    public int getGameId() {
        return gameId;
    }

    public String getColor() {
        return color;
    }
}