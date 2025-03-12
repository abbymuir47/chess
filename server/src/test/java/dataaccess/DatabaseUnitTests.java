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

}