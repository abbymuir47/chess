package server;

import spark.*;

public class Server {

    private final Server service;

    /* # tbh not sure why I need this constructor, but they have it in pet shop
    public Server(Server service){
        this.service = service;
    }
    */

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        // endpoint 1: register
        Spark.post("/user", this::register);
        // endpoint 2: login
        Spark.post("/session", this::login);
        // endpoint 3: logout
        Spark.delete("/session/:authToken", this::logout);
        // endpoint 4: list games
        Spark.get("/game/:authToken", this::listGames);
        // endpoint 5: create game
        Spark.post("/game/:authToken", this::createGame);
        // endpoint 6: join game
        Spark.put("/game/:authToken", this::joinGame);
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
        return null;
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
    // why create a constructor? why'd they do that with petshop?
    // how to pass in authToken - the header gets passed in? how is the body passed in, through the request?
    // what are the Pet objects / what's the Pet class being used?
    // Spark.exception
    // Spark.awaitInitialization()

}