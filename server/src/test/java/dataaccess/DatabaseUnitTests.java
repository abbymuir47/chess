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
    public void createUserSuccess() throws SQLException{
        UserData newUser = new UserData("myUser", "myPassword", "myEmail");
        System.out.println("create:" + sqlUserDataAccess.createUser(newUser));
        System.out.println("get:" + sqlUserDataAccess.getUser("myUser"));
    }

}