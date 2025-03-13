package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class SqlUserDataAccess implements UserDAO{

    @Override
    public UserData getUser(String username) throws SQLException {
        UserData myUser;
        String query = "SELECT * FROM user WHERE username = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                var rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    myUser = new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                    return myUser;
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public UserData createUser(UserData user) throws SQLException {
        String query = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        //System.out.println("in create user");
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, user.username());
                preparedStatement.setString(2, user.password());
                preparedStatement.setString(3, user.email());
                preparedStatement.executeUpdate();
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public void clearUserDAO() throws DataAccessException {
        String query = "TRUNCATE TABLE user";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
