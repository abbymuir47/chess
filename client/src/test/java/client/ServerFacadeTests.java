package client;

import dataaccess.DataAccessException;
import exception.ResponseException;
import handlermodel.RegisterRequest;
import handlermodel.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        String url = ("http://localhost:" + port + "/");
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(url);
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
        RegisterRequest req3 = new RegisterRequest("player2", "password", "p3@email.com");
        assertThrows(ResponseException.class, ()-> facade.register(req3));
    }

    @Test
    public void loginSuccess() throws ResponseException {
        LoginRequest req = new LoginRequest("player1", "password");
        LoginResult res = facade.login(req);
        assertTrue(res.authToken().length() > 10);
    }

    @Test
    public void loginFail(){
    }

    @Test
    public void createGameSuccess(){
    }

    @Test
    public void createGameFail(){
    }

    @Test
    public void listGamesSuccess(){
    }

    @Test
    public void listGamesFail(){
    }

    @Test
    public void joinGameSuccess(){
    }

    @Test
    public void joinGameFail(){
    }

}