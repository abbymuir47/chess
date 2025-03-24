package service;

import dataaccess.*;
import org.junit.jupiter.api.*;
import org.opentest4j.AssertionFailedError;

import java.sql.SQLException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UnitTests {

    MemoryUserDataAccess memoryUserDataAccess = new MemoryUserDataAccess();
    MemoryAuthDataAccess memoryAuthDataAccess = new MemoryAuthDataAccess();
    MemoryGameDataAccess memoryGameDataAccess = new MemoryGameDataAccess();
    UserService userService = new UserService(memoryUserDataAccess, memoryAuthDataAccess);
    AuthService authService = new AuthService(memoryUserDataAccess, memoryAuthDataAccess);
    GameService gameService = new GameService(memoryUserDataAccess, memoryAuthDataAccess, memoryGameDataAccess);

    @BeforeAll
    public static void init() {
    }

    @Test
    public void registerTestSuccess() throws DataAccessException, SQLException {
        RegisterRequest newUser = new RegisterRequest("myUser", "myPassword", "myEmail");
        RegisterResult result = userService.register(newUser);

        Assertions.assertEquals(newUser.username(), result.username(),
                "Response did not give the same username as user");
        Assertions.assertNotNull(result.authToken(), "Response did not return authentication String");
    }

    @Test
    public void registerTestFail() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("myUser", null, "myEmail");

        DataAccessException e = Assertions.assertThrows(DataAccessException.class, ()-> userService.register(newUser));

        Assertions.assertEquals("Error: bad request - all fields must be filled",
                e.getMessage());
    }

    @Test
    public void loginSuccess() throws DataAccessException, SQLException {
        RegisterRequest newUser = new RegisterRequest("myUser", "myPassword", "myEmail");
        userService.register(newUser);

        LoginRequest req = new LoginRequest("myUser", "myPassword");
        LoginResult result = userService.login(req);

        Assertions.assertEquals(newUser.password(), req.password(),
                "Request did not give the same password as user");
        Assertions.assertNotNull(result.authToken(), "Response did not return authentication String");
    }

    @Test
    public void loginFail() throws DataAccessException, SQLException {
        RegisterRequest newUser = new RegisterRequest("myUser", "myPassword", "myEmail");
        userService.register(newUser);

        LoginRequest req = new LoginRequest("myUser", "wrongPassword");

        DataAccessException e = Assertions.assertThrows(DataAccessException.class, ()-> userService.login(req));

        Assertions.assertEquals("Error: unauthorized",
                e.getMessage());
    }

    @Test
    public void logoutSuccess() throws DataAccessException, SQLException {
        RegisterRequest newUser = new RegisterRequest("myUser", "myPassword", "myEmail");
        RegisterResult result = userService.register(newUser);

        String auth = result.authToken();
        userService.logout(auth);

        Assertions.assertNotNull(result.authToken(), "Response did not return authentication String");
    }

    @Test
    public void logoutFail() throws DataAccessException {
        DataAccessException e = Assertions.assertThrows(DataAccessException.class, ()-> userService.logout(null));

        Assertions.assertEquals("Error: unauthorized",
                e.getMessage());
    }

    @Test
    public void createGameSuccess() throws DataAccessException {
        CreateRequest req = new CreateRequest("myGame");
        CreateResult result = gameService.creategame(req);
        int id = 1;

        Assertions.assertEquals(result.gameID(), id,
                "Result did not give the same ID as expected");
    }

    @Test
    public void createGameFail() throws AssertionFailedError, DataAccessException {
        CreateRequest req = new CreateRequest("myGame");
        CreateResult result = gameService.creategame(req);
        int id = 2;

        AssertionFailedError e = Assertions.assertThrows(AssertionFailedError.class, () ->
                Assertions.assertEquals(result.gameID(), id, "Result did not give the same ID as expected"));
    }

    @Test
    public void joingameSuccess() throws DataAccessException, SQLException {
        RegisterRequest newUser = new RegisterRequest("ExistingUser", "myPassword", "myEmail");
        RegisterResult result = userService.register(newUser);
        String existingAuth = result.authToken();

        CreateRequest createRequest = new CreateRequest("myGame");
        gameService.creategame(createRequest);

        JoinRequest req = new JoinRequest("WHITE", 1);
        gameService.joingame(req, "ExistingUser");
    }

    @Test
    public void joingameFail() throws DataAccessException, SQLException {
        RegisterRequest newUser = new RegisterRequest("ExistingUser", "myPassword", "myEmail");
        RegisterResult result = userService.register(newUser);
        String existingAuth = result.authToken();

        CreateRequest createRequest = new CreateRequest("myGame");
        gameService.creategame(createRequest);

        JoinRequest req = new JoinRequest("BLACK", 1);
        gameService.joingame(req, "ExistingUser");

        JoinRequest secondReq = new JoinRequest("BLACK", 1);

        DataAccessException e = Assertions.assertThrows(DataAccessException.class, ()-> gameService.joingame(secondReq, "NewUser"));

        Assertions.assertEquals("Error: black color already taken",
                e.getMessage());
    }

    @Test
    public void listGamesSuccess() throws DataAccessException, SQLException {
        RegisterRequest newUser = new RegisterRequest("whiteUser", "myPassword", "myEmail");
        RegisterResult user = userService.register(newUser);

        CreateResult game1 = gameService.creategame(new CreateRequest("game1"));
        CreateResult game2 = gameService.creategame(new CreateRequest("game2"));

        ListResult result = gameService.listgames(user.authToken());

        Assertions.assertEquals(result.games().getFirst().gameID(), 1,
                "Response did not give the same gameID as expected");
        Assertions.assertEquals(result.games().get(1).gameID(), 2,
                "Response did not give the same gameID as expected");
    }

    @Test
    public void listGamesFail() throws DataAccessException, SQLException {
        RegisterRequest newUser = new RegisterRequest("whiteUser", "myPassword", "myEmail");
        RegisterResult user = userService.register(newUser);

        CreateResult game1 = gameService.creategame(new CreateRequest("game1"));
        CreateResult game2 = gameService.creategame(new CreateRequest("game2"));

        ListResult result = gameService.listgames(user.authToken());

        AssertionFailedError e = Assertions.assertThrows(AssertionFailedError.class, () ->
                Assertions.assertEquals(result.games().getFirst().gameID(), 2, "Result did not give the same ID as expected"));

        AssertionFailedError f = Assertions.assertThrows(AssertionFailedError.class, () ->
                Assertions.assertEquals(result.games().get(1).gameID(), 1, "Result did not give the same ID as expected"));
    }

    @Test
    public void clearTest() throws DataAccessException {
        userService.clear();
        authService.clear();
        gameService.clear();
    }

}