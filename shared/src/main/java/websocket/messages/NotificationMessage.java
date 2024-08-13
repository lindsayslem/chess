package websocket.messages;

import com.google.gson.Gson;

public class NotificationMessage {
    public enum Type {
        PLAYER_JOINED,
        PLAYER_LEFT,
        PLAYER_MOVED,
        PLAYER_RESIGNED
    }

    private Type type;
    private String message;

    // Constructor with Type and message parameters
    public NotificationMessage(Type type, String message) {
        this.type = type;
        this.message = message;
    }

    // Getter methods for the type and message
    public Type getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}