package dataaccess;

import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserDataDAO implements IUserDataDAO {
    private final Map<String, UserData> userDataMap = new HashMap<>();

    @Override
    public UserData createUser(UserData user) throws DataAccessException {
        if(userDataMap.containsKey(user.username())){
            throw new DataAccessException("Username already exists");
        }
        userDataMap.put(user.username(), user);
        return user;
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
    public void clearUser() throws DataAccessException{
        userDataMap.clear();
    }
}
