package dataaccess;

import com.google.gson.Gson;
import model.GameData;
import chess.ChessGame;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static dataaccess.DatabaseSetup.executeUpdate;

public class MySqlGameDataDAO implements IGameDataDAO {

    public MySqlGameDataDAO() throws DataAccessException {
        DatabaseSetup.configureDatabase();
    }

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        if (gameName == null || gameName.trim().isEmpty()) {
            throw new DataAccessException("Game name cannot be null or empty.");
        }

        var statement = "INSERT INTO games (gameName, whiteUsername, blackUsername, game) VALUES (?, ?, ?, ?)";
        var check = "SELECT COUNT(*) FROM games WHERE gameName = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            // Disable auto-commit mode
            conn.setAutoCommit(false);
            System.out.println("Auto-commit disabled.");

            // Check if the game already exists
            try (PreparedStatement checkStmt = conn.prepareStatement(check)) {
                checkStmt.setString(1, gameName);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("Game with the same name already exists.");
                        throw new DataAccessException("Game with the same name already exists.");
                    }
                }
            }
            System.out.println("Game does not exist, proceeding to insert.");

            // Insert the new game
            try (PreparedStatement stmt = conn.prepareStatement(statement, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, gameName);
                stmt.setNull(2, Types.VARCHAR); // whiteUsername
                stmt.setNull(3, Types.VARCHAR); // blackUsername
                stmt.setString(4, "{}"); // Assuming game data as an empty JSON object
                int rowsInserted = stmt.executeUpdate(); // This is where the row is supposed to be inserted
                System.out.println("Rows inserted: " + rowsInserted); // Debug statement

                if (rowsInserted == 0) {
                    conn.rollback();
                    System.out.println("Creating game failed, no rows affected. Rolling back.");
                    throw new DataAccessException("Creating game failed, no rows affected.");
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int gameID = generatedKeys.getInt(1);
                        conn.commit();
                        System.out.println("Created game with ID: " + gameID); // Debug statement
                        return new GameData(gameID, null, null, gameName, new ChessGame());
                    } else {
                        conn.rollback();
                        System.out.println("Creating game failed, no ID obtained. Rolling back.");
                        throw new DataAccessException("Creating game failed, no ID obtained.");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to create game: " + e.getMessage());
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        var statement = "SELECT * FROM games WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(statement)) {
                stmt.setInt(1, gameID);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String gameName = rs.getString("gameName");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameDataJson = rs.getString("game");
                        ChessGame game = new Gson().fromJson(gameDataJson, ChessGame.class);
                        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                    } else {
                        throw new DataAccessException("Game not found with ID: " + gameID);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to retrieve game: " + e.getMessage());
        }
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        var statement = "UPDATE games SET whiteUsername=?, blackUsername=?, gameName=?, game=? WHERE gameID=?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setString(1, game.getWhiteUsername());
            ps.setString(2, game.getBlackUsername());
            ps.setString(3, game.getGameName());
            ps.setString(4, new Gson().toJson(game.getGame()));
            ps.setInt(5, game.getGameID());
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new DataAccessException("Game not found.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to update database: " + e.getMessage());
        }
    }

    @Override
    public Map<Integer, GameData> listGames() throws DataAccessException {
        var result = new HashMap<Integer, GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        var game = readGame(rs);
                        result.put(game.getGameID(), game);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    @Override
    public void clearGame() throws DataAccessException {
        var statement = "TRUNCATE TABLE games";
        executeUpdate(statement);
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var gameID = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var json = rs.getString("game");
        var game = new Gson().fromJson(json, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }
}
