package server.websocket;

import com.google.gson.Gson;
import dataaccess.SqlAuthDataAccess;
import exception.ResponseException;
import model.AuthData;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

import static websocket.messages.ServerMessage.ServerMessageType.ERROR;
import static websocket.messages.ServerMessage.ServerMessageType.NOTIFICATION;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private Gson gson = new Gson();
    public SqlAuthDataAccess sqlAuthDataAccess = new SqlAuthDataAccess();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            UserGameCommand command = gson.fromJson(message, UserGameCommand.class);

            String authToken = command.getAuthToken();

            if(authToken == null | authToken.isEmpty()){
                connections.sendMessage(session, new ErrorMessage(ERROR, "Error: enter authentication"));
                return;
            }

            AuthData auth = sqlAuthDataAccess.getAuth(authToken);

            if(auth == null){
                connections.sendMessage(session, new ErrorMessage(ERROR, "Error: unauthorized"));
                return;
            }

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
            connections.sendMessage(session, new ErrorMessage(ERROR, "Error: " + ex.getMessage()));
        }
    }

    private void connect(Session session, String username, UserGameCommand command) throws IOException {
        System.out.println("connect request made");
        connections.add(command.getGameID(), username, session);
        NotificationMessage connectMessage = new NotificationMessage(NOTIFICATION, String.format("%s joined the game", username));
        System.out.println("connect message: " + connectMessage);
        connections.broadcast(command.getGameID(), username, connectMessage);
        System.out.println("broadcast completed");
    }
    private void makeMove(Session session, String username, UserGameCommand command){}
    private void leaveGame(Session session, String username, UserGameCommand command){}
    private void resign(Session session, String username, UserGameCommand command){}

}