package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

import static dataaccess.DatabaseSetup.executeUpdate;

public class MySqlUserDataDAO implements IUserDataDAO {

    public MySqlUserDataDAO() throws DataAccessException {
        DatabaseSetup.configureDatabase();
    }

    @Override
    public UserData createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
            conn.setAutoCommit(false);
            ps.setString(1, user.username());
            ps.setString(2, user.email());
            ps.setString(3, hashedPassword);
            ps.executeUpdate();

            try (var rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    rs.getInt(1);
                } else {
                    conn.rollback();
                    throw new DataAccessException(String.format("Unable to generate key for user: %s", user.username()));
                }
            }
            conn.commit();
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to create user: %s", e.getMessage()));
        }
        return new UserData(user.username(), hashedPassword, user.email());
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        UserData user = null;
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error occurred while getting user");
        }

        if (user == null) {
            throw new DataAccessException("User not found.");
        }
        return user;
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

    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email);
    }
}
