package model;

public class JoinGameRequest {
    private int gameId;
    private String color;

    public JoinGameRequest(int gameId, String color) {
        this.gameId = gameId;
        this.color = color;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}