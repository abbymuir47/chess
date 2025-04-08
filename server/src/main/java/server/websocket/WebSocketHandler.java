package server.websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;

public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private Gson gson = new Gson();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {
            UserGameCommand command = gson.fromJson(message, UserGameCommand.class);

            String authToken = command.getAuthToken();

            saveSession(command.getGameID(), session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command);
                case MAKE_MOVE -> makeMove(session, username, command);
                case LEAVE -> leaveGame(session, username, command);
                case RESIGN -> resign(session, username, command);
            }
        } catch (ResponseException ex) {
            // Serializes and sends the error message
            sendMessage(session.getRemote(), new ErrorMessage("Error: unauthorized"));
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
        }
    }

    private void connect(Session session, String username, UserGameCommand command){}
    private void makeMove(Session session, String username, UserGameCommand command){}
    private void leaveGame(Session session, String username, UserGameCommand command){}
    private void resign(Session session, String username, UserGameCommand command){}

}