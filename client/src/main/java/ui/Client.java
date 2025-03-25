package ui;

import chess.ChessGame;
import exception.ResponseException;
import handlermodel.*;
import model.GameData;
import server.ServerFacade;

import java.awt.*;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static chess.ChessGame.TeamColor.WHITE;
import static ui.ChessBoard.ColorPerspective.*;
import static ui.EscapeSequences.*;

public class Client {

    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.LOGGEDOUT;
    private Map<Integer, GameData> gameMap = new HashMap<>();
    private static PrintStream out;
    private ChessBoard.ColorPerspective defaultPerspective = WHITE_PLAYER;
    private ChessBoard.ColorPerspective currPerspective;

    public Client(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }

    public void run() {
        System.out.println("Welcome to chess. Type help to get started.");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = this.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + state + " >>> " + SET_TEXT_COLOR_GREEN);
    }

    public String eval(String input) throws ResponseException {
        var tokens = input.split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "register" -> register(params);
            case "login" -> login(params);
            case "create" -> createGame(params);
            case "list" -> listGames(params);
            case "observe" -> observeGame(params);
            case "join" -> joinGame(params);
            case "logout" -> logOut(params);
            case "quit" -> "quit";
            default -> help();
        };
    }

    private String register(String[] params) throws ResponseException {
        if(params.length == 3) {
            RegisterRequest req = new RegisterRequest(params[0], params[1], params[2]);
            //System.out.println("in client, about to make request: " + req);
            RegisterResult res = server.register(req);
            //System.out.println(res);
            return ("You registered as " + res.username());
        }
        throw new ResponseException("Expected: <username> <password> <email>");
    }

    private String login(String[] params) throws ResponseException {
        if(params.length == 2) {
            LoginRequest req = new LoginRequest(params[0], params[1]);
            LoginResult res = server.login(req);
            state = State.LOGGEDIN;
            return ("You logged in as " + res.username() + ". Type help to see more actions.\n");
        }
        throw new ResponseException("Expected: <username> <password>");
    }

    private String createGame(String[] params) throws ResponseException {
        assertSignedIn();
        if(params.length == 1) {
            CreateRequest req = new CreateRequest(params[0]);
            CreateResult res = server.createGame(req);
            return ("Your new game ID is: " + res.gameID());
        }
        throw new ResponseException("Expected: <game name>");
    }

    private String listGames(String[] params) throws ResponseException {
        assertSignedIn();
        if(params.length == 0) {
            ListResult res = server.listGames();
            ArrayList<GameData> games = res.games();

            int count = 1;
            for (GameData game : games){
                gameMap.put(count, game);

                out.print(SET_BG_COLOR_BLACK);
                out.print(SET_TEXT_COLOR_MAGENTA);
                out.println("Game " + count + ": " + game.gameName());
                out.println("White player: " + game.whiteUsername());
                out.println("Black player: " + game.blackUsername() + "\n");
                count++;
            }
            return ("You listed all the games");
        }
        throw new ResponseException("Expected: list");
    }

    private String observeGame(String[] params) throws ResponseException {
        assertSignedIn();
        if(params.length == 1) {
            ListResult res = server.listGames();
            ArrayList<GameData> games = res.games();
            int id = Integer.parseInt(params[0]);
            drawCurrentBoard(id, defaultPerspective);
            return ("You are observing game " + id);
        }
        throw new ResponseException("Expected: <game ID>");
    }

    private String joinGame(String[] params) throws ResponseException {
        assertSignedIn();
        int id = Integer.parseInt(params[0]);
        String color = params[1];

        if(!color.equals("WHITE") && !color.equals("BLACK")){
            throw new ResponseException("Expected: WHITE or BLACK");
        }
        else{
            if(color.equals("WHITE")){
                currPerspective = WHITE_PLAYER;
            }
            else{
                currPerspective = BLACK_PLAYER;
            }
        }

        if(params.length == 2) {
            GameData currGame = gameMap.get(id);
            int currId = currGame.gameID();

            JoinRequest req = new JoinRequest(color, currId);
            server.joinGame(req);
            drawCurrentBoard(id, currPerspective);
            return ("Game " + id + " joined.");
        }
        throw new ResponseException("Expected: <game ID> <WHITE/BLACK>");
    }

    private String logOut(String[] params) throws ResponseException {
        assertSignedIn();
        state = State.LOGGEDOUT;
        return "Logged out successfully";
    }

    public String help() {
        if (state == State.LOGGEDOUT) {
            return """
                    Please select an option:
                    Register user: enter "register <username> <password> <email>"
                    Login: enter "login <username> <password>
                    Quit: "quit"
                    Help menu: "help"
                    """;
        }
        return """
                Please select an option:
                Create a game: enter "create <game name>"
                List games: enter "list"
                Observe game: enter "observe <game ID>"
                Join game: enter "join <game ID> <WHITE/BLACK>"
                Logout: "logout"
                Quit: "quit"
                Help menu: "help"
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.LOGGEDOUT) {
            throw new ResponseException("You must sign in");
        }
    }

    private void drawCurrentBoard(int id, ChessBoard.ColorPerspective perspective) throws ResponseException {
        try{
            GameData currGame = gameMap.get(id);
            getAndDrawBoard(currGame, perspective);
        }
        catch(Exception e){
            throw new ResponseException("Game ID not found. Type 'list' to get a list of current games");
        }
    }

    private static void getAndDrawBoard(GameData game, ChessBoard.ColorPerspective perspective) {
        ChessGame currGame = game.game();
        chess.ChessBoard chessClassBoard = currGame.getBoard();
        ChessBoard uiBoard = new ChessBoard(out, chessClassBoard, perspective);
        uiBoard.drawBoard();
    }
}
