package websocket.commands;

public class LeaveCommand extends UserGameCommand {
    public LeaveCommand(String authToken, String username) {
        super(authToken);
        this.commandType = CommandType.LEAVE;
        this.username = username;
    }

    private final String username;

    public String getUsername() {
        return username;
    }
}