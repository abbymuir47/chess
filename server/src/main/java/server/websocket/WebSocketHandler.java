package server.websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import model.*;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;
import websocket.messages.*;

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
            case MAKE_MOVE -> makeMove(session, username, message);
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

        //if a user is a player:
        if(isPlayer(username, gameData)){
            String joinMessage = username + " joined game " + gameID;
            alertOtherUsers(session, username, command, joinMessage, gameID);

            //send load game message back to the user
            LoadGameMessage gameMessage = new LoadGameMessage(LOAD_GAME, game);
            connections.sendMessage(session, gameMessage);
        }
        else{
            String observeMessage = username + " is now observing game " + gameID;
            alertOtherUsers(session, username, command, observeMessage, gameID);
        }
    }

    private void alertOtherUsers(Session session, String username, UserGameCommand command, String message, int gameID) throws IOException {
        connections.add(command.getGameID(), username, session);
        NotificationMessage connectMessage = new NotificationMessage(NOTIFICATION, message);
        connections.broadcast(gameID, username, connectMessage);
    }

    private void makeMove(Session session, String username, String message){
        MakeMoveCommand moveCommand = gson.fromJson(message, MakeMoveCommand.class);
        ChessMove move = moveCommand.getChessMove();
    }
    private void leaveGame(Session session, String username, UserGameCommand command) throws DataAccessException, IOException {
        int gameID = command.getGameID();
        GameData originalGame = sqlGameDataAccess.getGame(gameID);
        if (originalGame == null) {
            connections.sendMessage(session, new ErrorMessage(ERROR, "Error: enter a valid game ID"));
            return;
        }

        GameData updatedGame;
        if(isPlayer(username, originalGame)){
            NotificationMessage leaveMessage = new NotificationMessage(NOTIFICATION, String.format("%s left the game", username));
            if(originalGame.whiteUsername()!=null && originalGame.whiteUsername().equals(username)){
                updatedGame = new GameData(originalGame.gameID(), null, originalGame.blackUsername(), originalGame.gameName(), originalGame.game());
                sqlGameDataAccess.updateGame(updatedGame);
                connections.remove(gameID, username);
                connections.broadcast(gameID, username, leaveMessage);
            }
            else if(originalGame.blackUsername()!=null && originalGame.blackUsername().equals(username)){
                updatedGame = new GameData(originalGame.gameID(), originalGame.whiteUsername(), null, originalGame.gameName(), originalGame.game());
                sqlGameDataAccess.updateGame(updatedGame);
                connections.remove(gameID, username);
                connections.broadcast(gameID, username, leaveMessage);
            }
            else{
                connections.sendMessage(session, new ErrorMessage(ERROR, "Error: user not found in this game"));
            }
        }
    }

    private void resign(Session session, String username, UserGameCommand command){}

    private static boolean isPlayer(String username, GameData game) {
        if(game.whiteUsername()!=null && game.whiteUsername().equals(username)) {
            return true;
        }
        else if(game.blackUsername()!=null && game.blackUsername().equals(username)) {
            return true;
        }
        else{
            return false;
        }
    }
}