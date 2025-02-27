package server;

import com.google.gson.Gson;
//import exception.ResponseException;
import handlerModel.*;

import service.*;
import spark.*;

public class Server {

    private final UserService userService;

    public Server(){
        this.userService = null;
    }

//    public Server(UserService service){
//        this.userService = service;
//    }

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

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object register(Request request, Response response) {
        RegisterRequest registerRequest = new Gson().fromJson(request.body(), RegisterRequest.class);
        RegisterResult registerResult = userService.register(registerRequest);
        return new Gson().toJson(registerResult);
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

    private Object clear(Request request, Response response) {
        return null;
    }

    //questions:
    // how to pass in authToken - the header gets passed in? how is the body passed in, through the request?
    // where / how to create authToken - in the handler? no prob not till the service
    // what are the Pet objects / what's the Pet class being used? - model class objects - should registerResult be a model class, and how does that fit in with UserData, AuthData, and GameData (which are the 3 that the spec told us we need to have)
    // Spark.exception - would be wise to create multiple exceptions
    // Spark.awaitInitialization()

}