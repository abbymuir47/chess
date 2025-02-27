package server;

import com.google.gson.Gson;
//import exception.ResponseException;
import dataaccess.*;
import handlerModel.*;

import model.UserData;
import service.*;
import spark.*;

public class Server {

    private final UserService userService;
    private final AuthService authService;
    private final GameService gameService;

    private final UserDAO userDataAccess;
    private final AuthDAO authDataAccess;
    private final GameDAO gameDataAccess;

    public Server(){
        this.userDataAccess = new UserDataAccess();
        this.authDataAccess = new AuthDataAccess();
        this.gameDataAccess = new GameDataAccess();
        this.userService = new UserService(userDataAccess,authDataAccess);
        this.authService = new AuthService(userDataAccess,authDataAccess);
        this.gameService = new GameService(userDataAccess,authDataAccess,gameDataAccess);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        // endpoint 1: register
        Spark.post("/user", this::register);
        // endpoint 2: login
        Spark.post("/session", this::login);
        // endpoint 3: logout
        Spark.delete("/session", this::logout);
        // endpoint 4: list games
        Spark.get("/game", this::listGames);
        // endpoint 5: create game
        Spark.post("/game", this::createGame);
        // endpoint 6: join game
        Spark.put("/game", this::joinGame);
        // endpoint 7: clear
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

    private Object register(Request request, Response response) throws DataAccessException {
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

    private Object login(Request request, Response response) {
        return null;
    }

    private Object logout(Request request, Response response) {
        return null;
    }

    private Object listGames(Request request, Response response) {
        return null;
    }

    private Object createGame(Request request, Response response) {
        return null;
    }

    private Object joinGame(Request request, Response response) {
        return null;
    }



    //questions:
    // how to pass in authToken - the header gets passed in? how is the body passed in, through the request?
    // where / how to create authToken - in the handler? no prob not till the service
    // what are the Pet objects / what's the Pet class being used? - model class objects - should registerResult be a model class, and how does that fit in with UserData, AuthData, and GameData (which are the 3 that the spec told us we need to have)
    // Spark.exception - would be wise to create multiple exceptions
    // Spark.awaitInitialization()

}