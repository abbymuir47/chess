package server;

import com.google.gson.Gson;
//import exception.ResponseException;
import dataaccess.*;
import handlermodel.*;

import model.AuthData;
import service.*;
import spark.*;

import java.sql.SQLException;

public class Server {

    private final UserService userService;
    private final AuthService authService;
    private final GameService gameService;

    private final UserDAO userDataAccess;
    private final AuthDAO authDataAccess;
    private final GameDAO gameDataAccess;

    public Server(){
        this.userDataAccess = new MemoryUserDataAccess();
        this.authDataAccess = new MemoryAuthDataAccess();
        this.gameDataAccess = new MemoryGameDataAccess();
        this.userService = new UserService(userDataAccess,authDataAccess);
        this.authService = new AuthService(userDataAccess,authDataAccess);
        this.gameService = new GameService(userDataAccess,authDataAccess,gameDataAccess);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clear);

        Spark.exception(DataAccessException.class, this::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(DataAccessException ex, Request req, Response res) {
        res.status(ex.getStatusCode());
        ExceptionMessage message = new ExceptionMessage(ex.getMessage());
        res.body(new Gson().toJson(message));
    }

    private Object register(Request request, Response response) throws DataAccessException, SQLException {
        RegisterRequest registerRequest = new Gson().fromJson(request.body(), RegisterRequest.class);
        RegisterResult registerResult = userService.register(registerRequest);
        return new Gson().toJson(registerResult);
    }

    private Object clear(Request request, Response response) throws DataAccessException {
        userService.clear();
        authService.clear();
        gameService.clear();
        return "";
    }

    private Object login(Request request, Response response) throws DataAccessException, SQLException {
        LoginRequest loginRequest = new Gson().fromJson(request.body(), LoginRequest.class);
        LoginResult loginResult = userService.login(loginRequest);
        return new Gson().toJson(loginResult);
    }

    private Object logout(Request request, Response response) throws DataAccessException {
        String authToken = request.headers("authorization");
        checkAuth(authToken);
        userService.logout(authToken);
        return "";
    }

    private Object listGames(Request request, Response response) throws DataAccessException {
        String authToken = request.headers("authorization");
        checkAuth(authToken);
        ListResult listResult = gameService.listgames(authToken);
        return new Gson().toJson(listResult);
    }

    private Object createGame(Request request, Response response) throws DataAccessException {
        String authToken = request.headers("authorization");
        checkAuth(authToken);
        CreateRequest createRequest = new Gson().fromJson(request.body(), CreateRequest.class);
        CreateResult createResult = gameService.creategame(createRequest);
        return new Gson().toJson(createResult);
    }

    private Object joinGame(Request request, Response response) throws DataAccessException {
        String authToken = request.headers("authorization");
        checkAuth(authToken);
        String currUsername = getUsername(authToken);

        JoinRequest joinRequest = new Gson().fromJson(request.body(), JoinRequest.class);
        gameService.joingame(joinRequest, currUsername);
        return "";
    }


    public boolean checkAuth (String authToken) throws DataAccessException {
        try{
            AuthData data = authDataAccess.getAuth(authToken);
            if(data.authToken() != null && !authToken.isEmpty()){
                return true;
            }
            else{
                throw new DataAccessException(401, "Error: unauthorized");
            }
        }
        catch (Exception e) {
            throw new DataAccessException(401, "Error: unauthorized");
        }
    }

    public String getUsername(String authToken) throws DataAccessException {
        checkAuth(authToken);
        AuthData auth = authDataAccess.getAuth(authToken);
        return auth.username();
    }



    // Spark.exception - would be wise to create multiple exceptions
    // Spark.awaitInitialization()

}