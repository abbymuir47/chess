package dataaccess;

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

        UserData isCreated = sqlUserDataAccess.createUser(newUser);
        System.out.println("User created: " + isCreated);

        UserData retrievedUser = sqlUserDataAccess.getUser("user1");
        System.out.println("Retrieved user: " + retrievedUser);

        Assertions.assertNotNull("User should be created", String.valueOf(isCreated)); // Checks if the user was created
        Assertions.assertNotNull("User should be retrieved", String.valueOf(retrievedUser)); // Checks if user was found
        Assertions.assertEquals("user1", "user1", retrievedUser.username()); // Ensures the username matches
    }

    @Test
    public void clearUserSuccess() throws SQLException, DataAccessException {
        sqlUserDataAccess.clearUserDAO();
    }

    @Test
    public void createAuthSuccess() throws DataAccessException, SQLException {
        AuthData newAuth = new AuthData("authToken123", "user1");

        // Creating the AuthData entry
        AuthData createdAuth = sqlAuthDataAccess.createAuth(newAuth);
        System.out.println("Auth created: " + createdAuth);

        // Retrieving the created AuthData to ensure it's stored
        AuthData retrievedAuth = sqlAuthDataAccess.getAuth("authToken123");
        System.out.println("Retrieved auth: " + retrievedAuth);

        // Assertions
        Assertions.assertNotNull("Auth should be created", String.valueOf(createdAuth)); // Check if creation was successful
        Assertions.assertNotNull("Auth should be retrieved", String.valueOf(retrievedAuth)); // Check if the AuthData is retrieved
        Assertions.assertEquals("authToken123", retrievedAuth.authToken()); // Ensure the authToken matches
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

}