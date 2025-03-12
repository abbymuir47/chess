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
                var rs = preparedStatement.executeQuery();  // Executes the query
                if (rs.next()) {
                    System.out.println("Username: " + rs.getString("username"));
                    System.out.println("Email: " + rs.getString("email"));
                    myUser = new UserData(rs.getString("username"), null, rs.getString("email"));
                    return myUser;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public UserData createUser(UserData user) throws SQLException {
        String query = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        System.out.println("in create user");
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

    }
}
