package dataaccess;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import chess.ChessGame;
import org.mindrot.jbcrypt.BCrypt;


import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;


public class MySqlDataAccess implements IGameDataDAO, IAuthDataDAO, IUserDataDAO {

    public MySqlDataAccess() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public UserData createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        int generatedId;
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.username());
            ps.setString(2, user.email());
            ps.setString(3, hashedPassword);
            ps.executeUpdate();

            try (var rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                } else {
                    throw new DataAccessException(String.format("Unable to generate key for user: %s", user.username()));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to create user: %s", e.getMessage()));
        }
        return new UserData(user.username(), hashedPassword, user.email());
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM users WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public boolean verifyUser(String username, String providedClearTestPassword) throws DataAccessException{
        UserData user = getUser(username);
        if(user != null){
            return BCrypt.checkpw(providedClearTestPassword, user.password());
        }
        return false;
    }

    @Override
    public void clearUser() throws DataAccessException {
        try {
            executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
            executeUpdate("DELETE FROM games");
            executeUpdate("DELETE FROM auth");
            executeUpdate("DELETE FROM users");
            executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
        } catch (DataAccessException e) {
            throw new DataAccessException(String.format("Unable to clear user data: %s", e.getMessage()));
        }
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

            // Check if the game already exists
            try (PreparedStatement checkStmt = conn.prepareStatement(check)) {
                checkStmt.setString(1, gameName);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new DataAccessException("Game with the same name already exists.");
                    }
                }
            }

            // Insert the new game
            try (PreparedStatement stmt = conn.prepareStatement(statement, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, gameName);
                stmt.setNull(2, Types.VARCHAR); // whiteUsername
                stmt.setNull(3, Types.VARCHAR); // blackUsername
                stmt.setString(4, "{}"); // Assuming game data as an empty JSON object
                int rowsInserted = stmt.executeUpdate();
                System.out.println("Rows inserted: " + rowsInserted); // Debug statement

                if (rowsInserted == 0) {
                    conn.rollback();
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
    public void updateGame(GameData game) throws DataAccessException{
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
                        System.out.println("Retrieved game from DB - ID: " + game.getGameID() + ", Name: " + game.getGameName());
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
    public void clearGame() throws DataAccessException{
        var statement = "TRUNCATE TABLE games";
        executeUpdate(statement);
    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        executeUpdate(statement, auth.authToken(), auth.username());
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM users WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(rs.getString("authToken"), rs.getString("username"));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken=?";
        executeUpdate(statement, authToken);
    }

    @Override
    public void clearAuth() throws DataAccessException{
        var statement = "TRUNCATE TABLE auth";
        executeUpdate(statement);
    }


    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email);
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var gameID = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var json = rs.getString("game");
        var game = new Gson().fromJson(json, ChessGame.class);
        System.out.println("Read game from ResultSet - ID: " + gameID + ", Name: " + gameName); // Debug statement
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param == null) {
                        ps.setNull(i + 1, Types.VARCHAR);
                    } else if (param instanceof String) {
                        ps.setString(i + 1, (String) param);
                    } else if (param instanceof Integer) {
                        ps.setInt(i + 1, (Integer) param);
                    }
                }
                int affectedRows = ps.executeUpdate();
                System.out.println("Affected Rows: " + affectedRows);

                try (var rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(format("Unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
              id INT NOT NULL AUTO_INCREMENT,
              username VARCHAR(50) NOT NULL UNIQUE,
              password VARCHAR(255) NOT NULL,
              email VARCHAR(50) NOT NULL UNIQUE,
              PRIMARY KEY (id)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS games (
              gameID INT NOT NULL AUTO_INCREMENT,
              whiteUsername VARCHAR(255) DEFAULT NULL,
              blackUsername VARCHAR(255) DEFAULT NULL,
              gameName VARCHAR(50) NOT NULL,
              game TEXT NOT NULL,
              PRIMARY KEY (gameID),
              FOREIGN KEY (whiteUsername) REFERENCES users(username),
              FOREIGN KEY (blackUsername) REFERENCES users(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS auth (
              authToken VARCHAR(255) NOT NULL,
              userID INT NOT NULL,
              PRIMARY KEY (authToken),
              FOREIGN KEY (userID) REFERENCES users(id)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """

    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
    }

}


