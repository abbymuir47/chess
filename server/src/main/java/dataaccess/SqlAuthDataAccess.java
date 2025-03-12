package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class SqlAuthDataAccess implements AuthDAO{
    @Override
    public AuthData getAuth(String authToken) throws SQLException, DataAccessException {
        AuthData myAuth;
        String query = "SELECT * FROM auth WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, authToken);
                var rs = preparedStatement.executeQuery();  // Executes the query
                if (rs.next()) {
                    //System.out.println("Username: " + rs.getString("username"));
                    //System.out.println("Email: " + rs.getString("email"));
                    myAuth = new AuthData(rs.getString("authToken"), rs.getString("username"));
                    return myAuth;
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public AuthData createAuth(AuthData auth) throws DataAccessException {
        String query = "INSERT INTO auth (authToken, username) VALUES (?, ?)";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, auth.authToken());
                preparedStatement.setString(2, auth.username());
                preparedStatement.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        return auth;
    }

    @Override
    public void clearAuthDAO() throws DataAccessException {
        String query = "TRUNCATE TABLE auth";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        String authToDelete = authToken;

        String query = "DELETE FROM auth WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, authToDelete);
                preparedStatement.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
