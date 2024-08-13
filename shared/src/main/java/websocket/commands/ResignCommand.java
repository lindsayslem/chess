package websocket.commands;

public class ResignCommand extends UserGameCommand {
    public ResignCommand(String authToken, String username) {
        super(authToken);
        this.commandType = CommandType.RESIGN;
        this.username = username;
    }

    private final String username;

    public String getUsername() {
        return username;
    }
}