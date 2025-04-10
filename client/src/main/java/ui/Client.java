package ui;

import chess.*;
import com.google.gson.Gson;
import exception.ResponseException;
import handlermodel.*;
import model.GameData;
import server.ServerFacade;
import ui.websocket.ServerMessageObserver;
import ui.websocket.WebSocketFacade;
import websocket.messages.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static chess.ChessPiece.PieceType.*;
import static ui.ChessBoard.ColorPerspective.*;
import static ui.EscapeSequences.*;

public class Client implements ServerMessageObserver {

    private final ServerFacade server;
    private WebSocketFacade websocket;
    private final String serverUrl;
    private State state = State.LOGGEDOUT;
    private Map<Integer, GameData> gameMap = new HashMap<>();
    private GameData currGame;
    private static PrintStream out;
    private ChessBoard.ColorPerspective defaultPerspective = WHITE_PLAYER;
    private ChessBoard.ColorPerspective currPerspective;
    private String currAuth;
    public Gson gson = new Gson();
    private int currGameID;


    public Client(String serverUrl) {
        this.serverUrl = serverUrl;
        this.out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        server = new ServerFacade(serverUrl);
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
        System.out.print("\n" + state + " >>> " + SET_TEXT_COLOR_GREEN + "\n");
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
            case "logout" -> logout(params);
            case "redraw" -> redraw(params);
            case "move" -> makeMove(params);
            case "highlight" -> highlightMoves(params);
            case "resign" -> resign(params);
            case "leave" -> leaveGame(params);
            case "quit" -> "quit";
            default -> help();
        };
    }

    private String register(String[] params) throws ResponseException {
        if(params.length == 3) {
            RegisterRequest req = new RegisterRequest(params[0], params[1], params[2]);
            RegisterResult res = server.register(req);
            currAuth = res.authToken();
            state = State.LOGGEDIN;
            return ("You registered as " + res.username());
        }
        throw new ResponseException("Expected: <username> <password> <email>");
    }

    private String login(String[] params) throws ResponseException {
        if(params.length == 2) {
            LoginRequest req = new LoginRequest(params[0], params[1]);
            LoginResult res = server.login(req);
            currAuth = res.authToken();
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
            ListResult list = server.listGames();
            int id = gameMap.size() +1;
            gameMap.put(id, list.games().getLast());
            return ("Your new game ID is: " + id);
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
            try{
                int id = Integer.parseInt(params[0]);
                websocket = new WebSocketFacade(serverUrl, this);
                websocket.observeGame(currAuth, id);
                //drawCurrentBoard(id, defaultPerspective, null);
                state = State.INGAME;
                currGameID = id;
                return ("You are observing game " + id);
            } catch (NumberFormatException e) {
                throw new ResponseException("Please input an integer as gameID");
            }
        }
        throw new ResponseException("Expected: <game ID>");
    }

    private String joinGame(String[] params) throws ResponseException {
        assertSignedIn();
        if(params.length == 2) {
            try{
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
                currGame = gameMap.get(id);
                if(gameMap.containsKey(id)){
                    int currId = currGame.gameID();

                    JoinRequest req = new JoinRequest(color, currId);
                    server.joinGame(req);
                    state = State.INGAME;
                    websocket = new WebSocketFacade(serverUrl, this);
                    currGameID = id;
                    websocket.joinGame(currAuth, currGameID);
                    return ("Game " + id + " joined.");
                }
                throw new ResponseException("Game not found. Please list games and try again.");
            }
            catch(NumberFormatException e){
                throw new ResponseException("Please input an integer as gameID");
            }

        }
        throw new ResponseException("Expected: <game ID> <WHITE/BLACK>");
    }

    private String logout(String[] params) throws ResponseException {
        if(params.length == 0){
            assertSignedIn();
            server.logout();
            return "Logged out successfully";
        }
        throw new ResponseException("Expected: logout");
    }

    private String redraw(String[] params) throws ResponseException {
        drawCurrentBoard(currGame.gameID(), currPerspective, null);
        return "board redrawn";
    }

    private String makeMove(String[] params) throws ResponseException {
        if(params.length == 1 || params.length == 3){
            String req = params[0];
            int startRow = getRow(req.substring(0,2));
            int startCol = getCol(req.substring(0,2));
            ChessPosition startPos = new ChessPosition(startRow, startCol);
            int endRow = getRow(req.substring(2,4));
            int endCol = getCol(req.substring(2,4));
            ChessPosition endPos = new ChessPosition(endRow, endCol);

            ChessMove move;
            if(params.length == 1){
                move = new ChessMove(startPos, endPos, null);
            }
            else{
                if(params[2].equals("Queen")){move = new ChessMove(startPos, endPos, QUEEN);}
                else if(params[2].equals("Knight")){move = new ChessMove(startPos, endPos, KNIGHT);}
                else if(params[2].equals("Rook")){move = new ChessMove(startPos, endPos, ROOK);}
                else if(params[2].equals("Bishop")){move = new ChessMove(startPos, endPos, BISHOP);}
                else{return "Please enter valid piece type";}
            }

            websocket.makeMove(currAuth, currGameID, move);
            return ("move made");
        }
        throw new ResponseException("Expected: move <c1c2> -> <Queen>");
    }

    private String highlightMoves(String[] params) throws ResponseException {
        if(params.length == 1) {
            String req = params[0];

            int row = getRow(req);
            int col = getCol(req);

            ChessPosition pos = new ChessPosition(row,col);
            ChessGame chessGame = currGame.game();

            Collection<ChessMove> moves = chessGame.validMoves(pos);
            int numMoves = moves.size();
            int[][] positions = new int[numMoves][2];
            int i=0;

            for (ChessMove move: moves){
                ChessPosition potentialMove = move.getEndPosition();
                positions[i][0] = potentialMove.getRow();
                positions[i][1] = potentialMove.getColumn();
                i++;
            }
            drawCurrentBoard(currGame.gameID(), currPerspective, positions);

            return ("You highlighted moves for " + req + "\n");
        }
        throw new ResponseException("Expected form: highlight <a1>");
    }

    private static int getRow(String req) throws ResponseException {
        int row;
        if (req.charAt(1) == '1'){row = 1;}
        else if (req.charAt(1) == '2') {row = 2;}
        else if (req.charAt(1) == '3') {row = 3;}
        else if (req.charAt(1) == '4') {row = 4;}
        else if (req.charAt(1) == '5') {row = 5;}
        else if (req.charAt(1) == '6') {row = 6;}
        else if (req.charAt(1) == '7') {row = 7;}
        else if (req.charAt(1) == '8') {row = 8;}
        else {
            throw new ResponseException("Please enter the square in form [abcdefgh][12345678]");
        }
        return row;
    }

    private static int getCol(String req) throws ResponseException {
        int col;
        if (req.charAt(0) == 'a'){col = 1;}
        else if (req.charAt(0) == 'b') {col = 2;}
        else if (req.charAt(0) == 'c') {col = 3;}
        else if (req.charAt(0) == 'd') {col = 4;}
        else if (req.charAt(0) == 'e') {col = 5;}
        else if (req.charAt(0) == 'f') {col = 6;}
        else if (req.charAt(0) == 'g') {col = 7;}
        else if (req.charAt(0) == 'h') {col = 8;}
        else {
            throw new ResponseException("Please enter the square in form [abcdefgh][12345678]");
        }
        return col;
    }

    private String resign(String[] params) throws ResponseException {
        websocket.resign(currAuth, currGameID);
        return "you have now resigned, the game is over";
    }

    private String leaveGame(String[] params) throws ResponseException {
        assertSignedIn();
        websocket.leaveGame(currAuth, currGameID);
        state = State.LOGGEDIN;
        return "game left successfully";
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
        else if (state == State.LOGGEDIN) {
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
        else if (state == State.INGAME){
            return """
                Please select an option:
                Redraw board: enter "redraw"
                Make move: enter move in the form "move c1c2 -> Queen" where -> Queen is optional, for pawn promotion and can be any piece type
                Highlight legal moves: enter "highlight c1"
                Resign: enter "resign"
                Leave game: "leave"
                Quit: "quit"
                Help menu: "help"
                """;
        }
        return "";
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.LOGGEDOUT) {
            throw new ResponseException("You must sign in");
        }
    }

    private void drawCurrentBoard(int id, ChessBoard.ColorPerspective perspective, int[][] highlights) throws ResponseException {
        try{
            GameData currGame = gameMap.get(id);
            getAndDrawBoard(currGame, perspective, highlights);
        }
        catch(Exception e){
            throw new ResponseException("Game ID not found. Type 'list' to get a list of current games");
        }
    }

    private static void getAndDrawBoard(GameData game, ChessBoard.ColorPerspective perspective, int[][] highlights) {
        ChessGame currGame = game.game();
        drawChessGame(perspective, highlights, currGame);
    }

    private static void drawChessGame(ChessBoard.ColorPerspective perspective, int[][] highlights, ChessGame currGame) {
        chess.ChessBoard chessClassBoard = currGame.getBoard();
        ChessBoard uiBoard = new ChessBoard(out, chessClassBoard, perspective, highlights);
        uiBoard.drawBoard();
    }

    @Override
    public void notify(String message) {

        ServerMessage serverMessage =
                gson.fromJson(message, ServerMessage.class);

        switch (serverMessage.getServerMessageType()) {
            case NOTIFICATION -> displayNotification(message);
            case ERROR -> displayError(message);
            case LOAD_GAME -> loadGame(message);
        }
    }

    private void displayNotification(String message){
        NotificationMessage notificationMessage = gson.fromJson(message, NotificationMessage.class);
        System.out.println(notificationMessage.getMessage());
    }
    private void displayError(String message){
        ErrorMessage errorMessage = gson.fromJson(message, ErrorMessage.class);
        out.print(SET_TEXT_COLOR_RED);
        out.println(errorMessage.getErrorMessage());
        out.print(SET_TEXT_COLOR_GREEN);
    }
    private void loadGame(String message){
        LoadGameMessage loadGameMessage = gson.fromJson(message, LoadGameMessage.class);

        if(loadGameMessage.getPlayerType().equals("white")){currPerspective = WHITE_PLAYER;}
        else if(loadGameMessage.getPlayerType().equals("black")){currPerspective = BLACK_PLAYER;}
        else{currPerspective = WHITE_PLAYER;}

        //i know this doesn't make sense because it's not truly updating it but need to figure out how to do so
        currGame = new GameData(currGameID, currGame.whiteUsername(), currGame.blackUsername(), currGame.gameName(), loadGameMessage.getGame());
        drawChessGame(currPerspective, null, loadGameMessage.getGame());
    }

}
