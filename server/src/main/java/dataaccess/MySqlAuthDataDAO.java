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
        System.out.println("AuthData inserted: " + auth.authToken() + ", " + auth.username());
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        if (authToken.startsWith("Bearer ")) {
            authToken = authToken.substring(7);
        }
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auth WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                System.out.println("Executing query: " + statement + " with authToken: " + authToken);
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        try {
                            String retrievedAuthToken = rs.getString("authToken");
                            String retrievedUsername = rs.getString("username");
                            System.out.println("AuthData found: " + retrievedAuthToken + ", " + retrievedUsername);
                            return new AuthData(retrievedAuthToken, retrievedUsername);
                        } catch (Exception e) {
                            System.out.println("Exception while processing result set: " + e.getMessage());
                            throw new DataAccessException(format("Unable to process data: %s", e.getMessage()));
                        }
                    } else {
                        System.out.println("AuthData not found for token: " + authToken);
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in getAuth: " + e.getMessage());
            throw new DataAccessException(format("Unable to read data: %s", e.getMessage()));
        }
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
