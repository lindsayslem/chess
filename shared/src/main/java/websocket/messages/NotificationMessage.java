package websocket.messages;

import com.google.gson.Gson;

public record NotificationMessage(Type type, String message) {
    public enum Type {
        PLAYER_JOINED,
        PLAYER_MOVED,
        PLAYER_LEFT,
        PLAYER_RESIGNED,
        CHECK,
        CHECKMATE
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}