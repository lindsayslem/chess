package websocket.commands;

public class MakeMoveCommand extends UserGameCommand {
    private final String username;
    private final String move;

    public MakeMoveCommand(String authToken, String username, String move) {
        super(authToken);
        this.username = username;
        this.move = move;
        this.commandType = CommandType.MAKE_MOVE;
    }

    public String getUsername() {
        return username;
    }

    public String getMove() {
        return move;
    }
}