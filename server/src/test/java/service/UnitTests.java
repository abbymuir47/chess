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
    public void registerTest() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("myUser", "myPassword", "myEmail");
        RegisterResult result = userService.register(newUser);

        Assertions.assertEquals(newUser.username(), result.username(),
                "Response did not give the same username as user");
        Assertions.assertNotNull(result.authToken(), "Response did not return authentication String");
    }

    @Test
    public void joingameTest() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("ExistingUser", "myPassword", "myEmail");
        RegisterResult result = userService.register(newUser);
        String existingAuth = result.authToken();

        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(1, null, "taken", "myGame", chessGame);

        CreateRequest createRequest = new CreateRequest("myGame");
        gameService.creategame(createRequest);

        JoinRequest req = new JoinRequest(WHITE, 1);
        gameService.joingame(req, "ExistingUser");

        ListResult listResult = gameService.listgames(existingAuth);

        System.out.println(listResult);
        //System.out.println(listResult.getGames()[0].getWhiteUsername());
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