package service;

import chess.ChessGame;
import dataaccess.*;
import handlermodel.*;
import model.GameData;
import org.junit.jupiter.api.*;
import passoff.model.TestListResult;

import static chess.ChessGame.TeamColor.WHITE;

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
    public void registerTestSuccess() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("myUser", "myPassword", "myEmail");
        RegisterResult result = userService.register(newUser);

        Assertions.assertEquals(newUser.username(), result.username(),
                "Response did not give the same username as user");
        Assertions.assertNotNull(result.authToken(), "Response did not return authentication String");
    }

    @Test
    public void registerTestFail() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("myUser", null, "myEmail");
        RegisterResult result = userService.register(newUser);

        Assertions.assertEquals(newUser.username(), result.username(),
                "Response did not give the same username as user");
        Assertions.assertNotNull(result.authToken(), "Response did not return authentication String");
    }

    @Test
    public void loginSuccess() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("myUser", "myPassword", "myEmail");
        userService.register(newUser);

        LoginRequest req = new LoginRequest("myUser", "myPassword");
        LoginResult result = userService.login(req);

        Assertions.assertEquals(newUser.password(), req.password(),
                "Request did not give the same password as user");
        Assertions.assertNotNull(result.authToken(), "Response did not return authentication String");
    }

    @Test
    public void logoutSuccess() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("myUser", "myPassword", "myEmail");
        RegisterResult result = userService.register(newUser);

        String auth = result.authToken();

        Assertions.assertEquals(newUser.username(), result.username(),
                "Response did not give the same username as user");
        Assertions.assertNotNull(result.authToken(), "Response did not return authentication String");
    }

    @Test
    public void createGameSuccess() throws DataAccessException {
        CreateRequest req = new CreateRequest("myGame");
        gameService.creategame(req);
    }

    @Test
    public void joingameSuccess() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("ExistingUser", "myPassword", "myEmail");
        RegisterResult result = userService.register(newUser);
        String existingAuth = result.authToken();

        CreateRequest createRequest = new CreateRequest("myGame");
        gameService.creategame(createRequest);

        JoinRequest req = new JoinRequest("WHITE", 1);
        gameService.joingame(req, "ExistingUser");
    }

    @Test
    public void listGamesSuccess() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("ExistingUser", "myPassword", "myEmail");
        RegisterResult result = userService.register(newUser);

        gameService.listgames(result.authToken());
    }

    @Test
    public void clearTest() throws DataAccessException {
        userService.clear();
        authService.clear();
        gameService.clear();
    }

    /*
    @Test
    public void joingameFail() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("ExistingUser", "myPassword", "myEmail");
        RegisterResult result = userService.register(newUser);
        String existingAuth = result.authToken();

       CreateRequest createRequest = new CreateRequest("myGame");
        gameService.creategame(createRequest);

        JoinRequest req = new JoinRequest("BLACK", 1);
        gameService.joingame(req, "ExistingUser");

        JoinRequest secondReq = new JoinRequest("BLACK", 1);
        gameService.joingame(req, "NewUser");
    }

     */

}