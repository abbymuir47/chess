package client;

import dataaccess.DataAccessException;
import exception.ResponseException;
import handlermodel.RegisterRequest;
import handlermodel.*;
import org.junit.jupiter.api.*;
import org.opentest4j.AssertionFailedError;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() throws ResponseException {
        server = new Server();
        var port = server.run(0);
        String url = ("http://localhost:" + port + "/");
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(url);
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    void registerSuccess() throws Exception {
        RegisterRequest req1 = new RegisterRequest("player1", "password", "p1@email.com");
        RegisterResult res = facade.register(req1);
        assertTrue(res.authToken().length() > 10);
    }

    @Test
    public void registerFail() throws ResponseException {
        RegisterRequest req2 = new RegisterRequest("player2", "password", "p2@email.com");
        facade.register(req2);
        RegisterRequest repeatReq = new RegisterRequest("player2", "password", "p3@email.com");
        assertThrows(ResponseException.class, ()-> facade.register(repeatReq));
    }

    @Test
    public void loginSuccess() throws ResponseException {
        RegisterRequest req3 = new RegisterRequest("player3", "password", "p3@email.com");
        facade.register(req3);
        LoginRequest req = new LoginRequest("player3", "password");
        LoginResult res = facade.login(req);
        assertTrue(res.authToken().length() > 10);
    }

    @Test
    public void loginFail() throws ResponseException{
        RegisterRequest req4 = new RegisterRequest("player4", "password", "p3@email.com");
        facade.register(req4);
        LoginRequest req = new LoginRequest("player4", "wrongPassword");
        assertThrows(ResponseException.class, ()-> facade.login(req));
    }

    @Test
    public void createGameSuccess() throws ResponseException {
        RegisterRequest req5 = new RegisterRequest("player5", "password", "p5@email.com");
        facade.register(req5);
        LoginRequest req = new LoginRequest("player5", "password");
        facade.login(req);

        CreateRequest gameReq = new CreateRequest("game2");
        CreateResult res = facade.createGame(gameReq);
        assertTrue(res.gameID() > 0);
    }

    @Test
    public void createGameFail() throws ResponseException {
        RegisterRequest req6 = new RegisterRequest("player6", "password", "p6@email.com");
        facade.register(req6);
        LoginRequest req = new LoginRequest("player6", "password");
        facade.login(req);

        CreateRequest nullReq = new CreateRequest(null);
        assertThrows(ResponseException.class, ()-> facade.createGame(nullReq));
    }

    @Test
    public void listGamesSuccess() throws ResponseException {
        RegisterRequest req7 = new RegisterRequest("player7", "password", "p7@email.com");
        facade.register(req7);
        LoginRequest req = new LoginRequest("player7", "password");
        facade.login(req);

        ListResult res = facade.listGames();
        assertTrue(res.games() != null);
    }

    @Test
    public void listGamesFail() throws ResponseException {
        assertThrows(ResponseException.class, ()-> facade.listGames());
    }

    @Test
    public void joinGameSuccess() throws ResponseException {
        facade.clear();

        RegisterRequest req8 = new RegisterRequest("player8", "password", "p8@email.com");
        facade.register(req8);
        LoginRequest req = new LoginRequest("player8", "password");
        facade.login(req);

        CreateRequest gameReq = new CreateRequest("game1");
        CreateResult res = facade.createGame(gameReq);
        facade.listGames();

        JoinRequest joinRequest = new JoinRequest("BLACK", 1);
        System.out.println(facade.joinGame(joinRequest));
    }

    @Test
    public void joinGameFail() throws ResponseException {
        RegisterRequest req9 = new RegisterRequest("player9", "password", "p9@email.com");
        facade.register(req9);
        LoginRequest req = new LoginRequest("player9", "password");
        facade.login(req);

        JoinRequest joinRequest = new JoinRequest("GREEN", 1);
        assertThrows(ResponseException.class, ()-> facade.joinGame(joinRequest));
    }

    @Test
    public void logoutSuccess() throws ResponseException {
        RegisterRequest req10 = new RegisterRequest("player10", "password", "p10@email.com");
        facade.register(req10);
        LoginRequest req = new LoginRequest("player10", "password");
        LoginResult res = facade.login(req);

        assertDoesNotThrow(() -> facade.logout());

        //assertTrue(res.authToken().length() > 10);
    }

    @Test
    public void logoutFail() throws ResponseException{
        RegisterRequest req11 = new RegisterRequest("player11", "password", "p11@email.com");
        facade.register(req11);
        LoginRequest req = new LoginRequest("player11", "password");
        LoginResult res = facade.login(req);

        facade.logout();

        assertThrows(ResponseException.class, ()-> facade.logout());
    }
}