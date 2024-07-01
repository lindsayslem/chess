package dataaccess;

import model.AuthData;

import java.sql.*;

import static java.lang.String.format;
import static dataaccess.DatabaseSetup.executeUpdate;

public class MySqlAuthDataDAO implements IAuthDataDAO {

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        executeUpdate(statement, auth.authToken(), auth.username());
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auth WHERE authToken=?";
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
        try (var conn = DatabaseManager.getConnection()) {
            // Check if the auth token exists
            var checkStmt = "SELECT COUNT(*) FROM auth WHERE authToken = ?";
            try (var ps = conn.prepareStatement(checkStmt)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        throw new DataAccessException("Auth token not found");
                    }
                }
            }

            // Proceed with deletion if the auth token exists
            var deleteStmt = "DELETE FROM auth WHERE authToken = ?";
            try (var ps = conn.prepareStatement(deleteStmt)) {
                ps.setString(1, authToken);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to update database");
        }
    }

    @Override
    public void clearAuth() throws DataAccessException {
        var statement = "TRUNCATE TABLE auth";
        executeUpdate(statement);
    }

}
