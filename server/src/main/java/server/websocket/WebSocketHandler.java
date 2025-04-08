package server.websocket;

import com.google.gson.Gson;
import dataaccess.SqlAuthDataAccess;
import exception.ResponseException;
import model.AuthData;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private Gson gson = new Gson();
    public SqlAuthDataAccess sqlAuthDataAccess;

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            UserGameCommand command = gson.fromJson(message, UserGameCommand.class);

            String authToken = command.getAuthToken();
            AuthData auth = sqlAuthDataAccess.getAuth(authToken);
            String username = auth.username();
            int gameID = command.getGameID();

            connections.add(gameID, username, session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command);
                case MAKE_MOVE -> makeMove(session, username, command);
                case LEAVE -> leaveGame(session, username, command);
                case RESIGN -> resign(session, username, command);
            }
        } catch (Exception ex) {
            connections.sendMessage(session, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + ex.getMessage()));
        }
    }

    private void connect(Session session, String username, UserGameCommand command){}
    private void makeMove(Session session, String username, UserGameCommand command){}
    private void leaveGame(Session session, String username, UserGameCommand command){}
    private void resign(Session session, String username, UserGameCommand command){}

}