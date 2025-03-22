package ui;

import exception.DataAccessException;

import java.util.Arrays;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Client {

    private String visitorName = null;
    //private final ServerFacade server;
    private final String serverUrl;
    private State state = State.LOGGEDIN;

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
            case "1" -> register(params);
            case "2" -> login(params);
            case "3" -> listGames(params);
            case "4" -> observeGame(params);
            case "5" -> joinGame(params);
            case "6" -> logOut(params);
            case "quit" -> "quit";
            default -> help();
        };
    }

    private String register(String[] params) {
        return "register request";
    }

    private String login(String[] params) {
        return "login request";
    }

    private String listGames(String[] params) {
        return "list game request";
    }

    private String observeGame(String[] params) {
        return "observe game request";
    }

    private String joinGame(String[] params) {
        return "join game request";
    }

    private String logOut(String[] params) {
        return "logout request";
    }

    public String help() {
        if (state == State.LOGGEDOUT) {
            return """
                    Please select an option:
                    1. Register user
                    2. Login
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

    private void assertSignedIn() throws DataAccessException {
        if (state == State.LOGGEDOUT) {
            throw new DataAccessException(400, "You must sign in");
        }
    }
}
