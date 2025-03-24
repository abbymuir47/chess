package ui;

import exception.ResponseException;
import handlermodel.*;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Client {

    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.LOGGEDOUT;

    public Client(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public void run() {
        System.out.println("Welcome to chess. Sign in to start.");
        System.out.print(this.help());

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
        System.out.print("\n" + RESET + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    public String eval(String input) throws ResponseException {
        var tokens = input.split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "register" -> register(params);
            case "login" -> login(params);
            case "3" -> listGames(params);
            case "4" -> observeGame(params);
            case "5" -> joinGame(params);
            case "6" -> logOut(params);
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
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    private String login(String[] params) throws ResponseException {
        if(params.length == 2) {
            LoginRequest req = new LoginRequest(params[0], params[1]);
            LoginResult res = server.login(req);
            System.out.println("authToken: " + res.authToken());
            state = State.LOGGEDIN;
            return ("You logged in as " + res.username());
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    private String listGames(String[] params) throws ResponseException {
        assertSignedIn();
        return "list game request";
    }

    private String observeGame(String[] params) throws ResponseException {
        assertSignedIn();
        return "observe game request";
    }

    private String joinGame(String[] params) throws ResponseException {
        assertSignedIn();
        return "join game request";
    }

    private String logOut(String[] params) throws ResponseException {
        assertSignedIn();
        return "logout request";
    }

    public String help() {
        if (state == State.LOGGEDOUT) {
            return """
                    Please select an option:
                    Register user: enter "register <username> <password> <email>"
                    Login: enter "login <username> <password>
                    """;
        }
        return """
                Please select an option:
                3. List games
                4. Observe game
                5. Join game
                6. Logout
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.LOGGEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}
