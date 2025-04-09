package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.sql.SQLException;

import static websocket.messages.ServerMessage.ServerMessageType.*;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private Gson gson = new Gson();
    public SqlAuthDataAccess sqlAuthDataAccess = new SqlAuthDataAccess();
    public SqlGameDataAccess sqlGameDataAccess = new SqlGameDataAccess();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, SQLException {
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
    }

    private void connect(Session session, String username, UserGameCommand command) throws IOException, DataAccessException {
        int gameID = command.getGameID();
        GameData gameData = sqlGameDataAccess.getGame(gameID);
        if (gameData == null) {
            connections.sendMessage(session, new ErrorMessage(ERROR, "Error: enter a valid game ID"));
            return;
        }
        ChessGame game = gameData.game();

        //notify other users that a certain user joined the game
        connections.add(command.getGameID(), username, session);
        NotificationMessage connectMessage = new NotificationMessage(NOTIFICATION, String.format("%s joined the game", username));
        connections.broadcast(gameID, username, connectMessage);

        //send load game message back to the user
        LoadGameMessage gameMessage = new LoadGameMessage(LOAD_GAME, game);
        connections.sendMessage(session, gameMessage);
    }

    private void makeMove(Session session, String username, UserGameCommand command){

    }
    private void leaveGame(Session session, String username, UserGameCommand command){

    }
    private void resign(Session session, String username, UserGameCommand command){}
}