package service;

import dataaccess.*;
import handlermodel.*;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UnitTests {

    UserDataAccess userDataAccess = new UserDataAccess();
    AuthDataAccess authDataAccess = new AuthDataAccess();
    GameDataAccess gameDataAccess = new GameDataAccess();
    UserService userService = new UserService(userDataAccess,authDataAccess);
    AuthService authService = new AuthService(userDataAccess,authDataAccess);
    GameService gameService = new GameService(userDataAccess,authDataAccess,gameDataAccess);

    @BeforeAll
    public static void init() {
    }

    @Test
    public void registerTest() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("myUser", "myPassword", "myEmail");
        RegisterResult result = userService.register(newUser);

        Assertions.assertEquals(newUser.username(), result.username(),
                "Response did not give the same username as user");
        Assertions.assertNotNull(result.authToken(), "Response did not return authentication String");
    }

    @Test
    public void logoutTest() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("myUser", "myPassword", "myEmail");
        RegisterResult result = userService.register(newUser);

        String auth = result.authToken();

        Assertions.assertEquals(newUser.username(), result.username(),
                "Response did not give the same username as user");
        Assertions.assertNotNull(result.authToken(), "Response did not return authentication String");
    }

}