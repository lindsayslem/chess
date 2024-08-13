package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MySqlAuthDataDAO;
import dataaccess.MySqlGameDataDAO;
import model.AuthData;
import model.GameData;
import model.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.ConnectCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.LeaveCommand;
import websocket.commands.ResignCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WebSocketHandler {
    private final MySqlAuthDataDAO authDataDAO = new MySqlAuthDataDAO();
    private final Map<String, Session> activeSessions = new ConcurrentHashMap<>();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        switch (command.getCommandType()) {
            case CONNECT -> handleConnect(session, new Gson().fromJson(message, ConnectCommand.class));
            case MAKE_MOVE -> handleMakeMove(session, new Gson().fromJson(message, MakeMoveCommand.class));
            case LEAVE -> handleLeave(session, new Gson().fromJson(message, LeaveCommand.class));
            case RESIGN -> handleResign(session, new Gson().fromJson(message, ResignCommand.class));
        }
    }

    private void handleConnect(Session session, ConnectCommand command) throws IOException {
        MySqlGameDataDAO gameDataDAO = new MySqlGameDataDAO(); // Instantiate your GameData DAO

        String username;
        String playerColor;
        GameData gameData;

        try {
            // Retrieve AuthData from the database using the authToken
            AuthData authData = authDataDAO.getAuth(command.getAuthToken());
            if (authData != null) {
                username = authData.username();
            } else {
                throw new IllegalArgumentException("Invalid auth token");
            }

            // Retrieve GameData to determine the player's color
            gameData = gameDataDAO.getGame(command.getGameID());
            if (gameData != null) {
                if (username.equals(gameData.getWhiteUsername())) {
                    playerColor = "WHITE";
                } else if (username.equals(gameData.getBlackUsername())) {
                    playerColor = "BLACK";
                } else {
                    throw new IllegalArgumentException("User not associated with this game");
                }
            } else {
                throw new IllegalArgumentException("Game not found");
            }

        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Database error occurred while fetching player name or game data");
        }

        // Use the retrieved username and playerColor for further processing
        System.out.println("Handling connect for username: " + username + " with color: " + playerColor);

        if (session == null) {
            throw new IllegalArgumentException("Session is null.");
        }
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username is null or empty.");
        }

        // Update the command's username and playerColor with the retrieved values
        command.setUsername(username);
        command.setPlayerColor(playerColor);

        // Store the active session
        activeSessions.put(username, session);

        // Step 1: Send the LOAD_GAME message to the connecting client
        ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        loadGameMessage.setGame(new Gson().toJson(gameData.getGame()));  // Include the game data in the message
        String loadGameMessageJson = new Gson().toJson(loadGameMessage);
        session.getRemote().sendString(loadGameMessageJson);

        // Step 2: Notify other clients about the new player
        String notificationMessage = String.format("%s connected as %s", username, playerColor);
        NotificationMessage notification = new NotificationMessage(NotificationMessage.Type.PLAYER_JOINED, notificationMessage);
        try {
            broadcastMessage(notificationMessage, NotificationMessage.Type.PLAYER_JOINED, session);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    // Utility method to broadcast a message to all active sessions
    private void broadcastMessage(String messageContent, NotificationMessage.Type type, Session originatingSession) throws ResponseException {
        try {
            // Construct the message and notification object
            String message = String.format("%s: %s", type, messageContent);
            NotificationMessage notification = new NotificationMessage(type, message);

            // Debugging: Log the message before broadcasting
            System.out.println("Broadcasting message: " + new Gson().toJson(notification));

            // Serialize the notification object to JSON
            String notificationJson = new Gson().toJson(notification);

            // Broadcast the message to all connected sessions except the originator
            for (Map.Entry<String, Session> entry : activeSessions.entrySet()) {
                Session playerSession = entry.getValue();
                if (playerSession.isOpen() && !playerSession.equals(originatingSession)) {
                    playerSession.getRemote().sendString(notificationJson);
                    System.out.println("Sending to session for user: " + entry.getKey());
                }
            }
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    private void handleMakeMove(Session session, MakeMoveCommand command) throws IOException {
        String moveMessage = String.format("%s made a move: %s", command.getUsername(), command.getMove());
        try {
            broadcastMessage(moveMessage, NotificationMessage.Type.PLAYER_MOVED, session);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleLeave(Session session, LeaveCommand command) throws IOException {
        activeSessions.remove(command.getUsername());
        String leaveMessage = String.format("%s has left the game", command.getUsername());
        try {
            broadcastMessage(leaveMessage, NotificationMessage.Type.PLAYER_LEFT, session);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleResign(Session session, ResignCommand command) throws IOException {
        String resignMessage = String.format("%s has resigned from the game", command.getUsername());
        try {
            broadcastMessage(resignMessage, NotificationMessage.Type.PLAYER_RESIGNED, session);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

}