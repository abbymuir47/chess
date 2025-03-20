package ui;

import exception.DataAccessException;

import java.util.Arrays;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Client {

    private String visitorName = null;
    //private final ServerFacade server;
    private final String serverUrl;
    private State state = State.LOGGEDOUT;

    public Client(String serverUrl) {
        //server = new ServerFacade(serverUrl);
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

    public String eval(String input) throws DataAccessException {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "register" -> register(params);
            case "login" -> login(params);
            case "list" -> listGames(params);
            case "observe" -> observeGame(params);
            case "join" -> joinGame(params);
            case "logout" -> logOut(params);
            case "quit" -> "quit";
            default -> help();
        };
    }

    private String register(String[] params) {
        return "";
    }

    private String login(String[] params) {
        return "";
    }

    private String listGames(String[] params) {
        return "";
    }

    private String observeGame(String[] params) {
        return "";
    }

    private String joinGame(String[] params) {
        return "";
    }

    private String logOut(String[] params) {
        return "";
    }

    public String help() {
        if (state == State.LOGGEDOUT) {
            return """
                    - login <yourname>
                    - quit
                    """;
        }
        return """
                Please select an option:
                1. Register user
                2. Login
                3. Join game
                4. Observe game
                5. List games
                6. Logout
                """;
    }

    private void assertSignedIn() throws DataAccessException {
        if (state == State.LOGGEDOUT) {
            throw new DataAccessException(400, "You must sign in");
        }
    }
}
