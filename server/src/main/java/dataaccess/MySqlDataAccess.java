package dataaccess;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;


import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;


public class MySqlDataAccess implements IGameDataDAO, IAuthDataDAO, IUserDataDAO {

    public MySqlDataAccess() throws DataAccessException {
        configureDatabase();
    }

    public UserData createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        var id = executeUpdate(statement, user.username(), user.password(), user.email());
        return new UserData(id, user.username(), user.password(), user.email());
    }

    public UserData getUser(int id) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, username, password, email FROM users WHERE id=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, id);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public Collection<UserData> listUsers() throws DataAccessException {
        var result = new ArrayList<UserData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, username, password, email FROM users";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readUser(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    public void deleteUser(Integer id) throws DataAccessException {
        var statement = "DELETE FROM users WHERE id=?";
        executeUpdate(statement, id);
    }

    public void deleteAllUsers() throws DataAccessException {
        var statement = "TRUNCATE user";
        executeUpdate(statement);
    }
    public GameData createGame(GameData game) throws DataAccessException {
        var statement = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        var json = new Gson().toJson(game);
        var id = executeUpdate(statement, game.getGameID(), game.getWhiteUsername(), game.getBlackUsername(), game.getGameName(), game.getGame(), json);
        return new GameData(id, game.getGameID(), game.getWhiteUsername(), game.getBlackUsername(), game.getGameName(), game.getGame());
    }

    public GameData getGame(int id) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE id=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, id);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public Collection<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, gameID, whiteUsername, blackUsername, gameName, game FROM games";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    public void deleteGame(Integer id) throws DataAccessException {
        var statement = "DELETE FROM games WHERE id=?";
        executeUpdate(statement, id);
    }

    public AuthData createAuth(AuthData auth) throws DataAccessException {
        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        return executeUpdate(statement, auth.getAuth(), auth.getUsername());
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM users WHERE id=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(rs.getString("authToken"), rs.getInt("userID"));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public void deleteGame(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken=?";
        executeUpdate(statement, authToken);
    }

    public void clear() throws DataAccessException{
    var statement = "TRUNCATE TABLE auth";
    executeUpdate(statement);
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var id = rs.getInt("id");
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email);
    }

    private UserData readGame(ResultSet rs) throws SQLException {
        var id = rs.getInt("id");
        var gameID = rs.getString("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var game = rs.getString("game");
        var json = rs.getString("game");
        var gameJson = new Gson().fromJson(json, GameData.class);
        return new GameData(id, gameID, whiteUsername, blackUsername, gameName, game, gameJson.getGame());

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof PetType p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  users (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(50) NOT NULL UNIQUE,
              `password` varchar(255) NOT NULL,
              `email` varchar(50) NOT NULL UNIQUE,
              PRIMARY KEY (`id`),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS  games (
              `id` int NOT NULL AUTO_INCREMENT,
              `gameID` int NOT NULL,
              `whiteUsername` varchar(50) NOT NULL UNIQUE,
              `blackUsername` varchar(50) NOT NULL UNIQUE,
              `gameName` int NOT NULL,
              `game` int NOT NULL,
              PRIMARY KEY (`id`),
              FOREIGN KEY ('whiteUsername') REFERENCES users('id'),
              FOREIGN KEY ('blackUsername') REFERENCES users('id'),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `authToken` varchar(255) NOT NULL,
              `userID` int NOT NULL,
              PRIMARY KEY (`authToken`),
              FOREIGN KEY (`authID`),
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
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
}

