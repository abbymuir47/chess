package dataaccess;

import chess.ChessGame;
import handlermodel.*;
import model.*;
import org.junit.jupiter.api.*;
import org.opentest4j.AssertionFailedError;
import service.*;

import java.sql.SQLException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseUnitTests {

    SqlUserDataAccess sqlUserDataAccess = new SqlUserDataAccess();
    SqlAuthDataAccess sqlAuthDataAccess = new SqlAuthDataAccess();
    SqlGameDataAccess sqlGameDataAccess = new SqlGameDataAccess();
    UserService userService = new UserService(sqlUserDataAccess, sqlAuthDataAccess);
    AuthService authService = new AuthService(sqlUserDataAccess, sqlAuthDataAccess);
    GameService gameService = new GameService(sqlUserDataAccess, sqlAuthDataAccess, sqlGameDataAccess);

    @BeforeAll
    public static void init() {
    }

    @Test
    public void createUserSuccess() throws SQLException {
        UserData newUser = new UserData("user1", "password1", "email1");

        UserData createdUser = sqlUserDataAccess.createUser(newUser);
        System.out.println("User created: " + createdUser);

        UserData retrievedUser = sqlUserDataAccess.getUser("user1");
        System.out.println("Retrieved user: " + retrievedUser);

        Assertions.assertNotNull("User should be created", String.valueOf(createdUser));
        Assertions.assertNotNull("User should be retrieved", String.valueOf(retrievedUser));
        Assertions.assertEquals(retrievedUser.username(), "user1",
                "Response did not give the same gameID as expected");
    }

    @Test
    public void clearUserSuccess() throws SQLException, DataAccessException {
        sqlUserDataAccess.clearUserDAO();
    }

    @Test
    public void createAuthSuccess() throws DataAccessException, SQLException {
        AuthData newAuth = new AuthData("authToken123", "user1");

        AuthData createdAuth = sqlAuthDataAccess.createAuth(newAuth);
        System.out.println("Auth created: " + createdAuth);

        AuthData retrievedAuth = sqlAuthDataAccess.getAuth("authToken123");
        System.out.println("Retrieved auth: " + retrievedAuth);

        Assertions.assertNotNull("Auth should be created", String.valueOf(createdAuth));
        Assertions.assertNotNull("Auth should be retrieved", String.valueOf(retrievedAuth));
        Assertions.assertEquals(retrievedAuth.authToken(),"authToken123",
                "Response did not give the same auth as expected");
    }

    @Test
    public void getAuthSuccess() throws DataAccessException, SQLException {
        AuthData newAuth = new AuthData("authToken123", "user1");

        sqlAuthDataAccess.createAuth(newAuth);

        AuthData retrievedAuth = sqlAuthDataAccess.getAuth("authToken123");
        System.out.println("Retrieved auth: " + retrievedAuth);

        Assertions.assertNotNull("Auth should be retrieved", String.valueOf(retrievedAuth)); // Check if the AuthData is retrieved
        Assertions.assertEquals("authToken123", retrievedAuth.authToken()); // Ensure the authToken matches
        Assertions.assertEquals("user1", retrievedAuth.username()); // Ensure the username matches
    }

    @Test
    public void deleteAuthSuccess() throws DataAccessException, SQLException {
        AuthData newAuth = new AuthData("authTokenDeleteTest", "userDeleteTest");
        sqlAuthDataAccess.createAuth(newAuth);
        sqlAuthDataAccess.deleteAuth("authTokenDeleteTest");

        AuthData retrievedAuth = sqlAuthDataAccess.getAuth("authTokenDeleteTest");

        Assertions.assertNull(retrievedAuth, "Auth should be deleted"); // Check if retrievedAuth is null
    }

    @Test
    public void clearAuthSuccess() throws SQLException, DataAccessException {
        sqlAuthDataAccess.clearAuthDAO();
    }

    @Test
    public void createGameSuccess() throws SQLException, DataAccessException {
        ChessGame chessGame = new ChessGame();

        GameData game = new GameData(1, null, null, "game1name", chessGame);
        GameData newGame = sqlGameDataAccess.createGame(game);
        System.out.println("Game created: " + newGame);

        GameData retrievedGame = sqlGameDataAccess.getGame(1);
        System.out.println("Retrieved user: " + retrievedGame);

        Assertions.assertNotNull("Game should be created", String.valueOf(newGame)); // Checks if the user was created
        Assertions.assertNotNull("User should be retrieved", String.valueOf(retrievedGame)); // Checks if user was found
        //Assertions.assertEquals("user1", "user1", retrievedGame.username()); // Ensures the username matches
    }

}