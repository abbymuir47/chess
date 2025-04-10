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
import java.util.Collection;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static websocket.messages.ServerMessage.ServerMessageType.*;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private Gson gson = new Gson();
    public SqlAuthDataAccess sqlAuthDataAccess = new SqlAuthDataAccess();
    public SqlGameDataAccess sqlGameDataAccess = new SqlGameDataAccess();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, SQLException, InvalidMoveException {
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
        System.out.println("connect command received from client");
        int gameID = command.getGameID();
        GameData gameData = getValidGameData(session, gameID);
        if (gameData == null) {return;}
        ChessGame game = gameData.game();

        //notify other users that a certain user joined the game
        connections.add(command.getGameID(), username, session);

        String playerType;
        if(gameData.whiteUsername()!=null && gameData.whiteUsername().equals(username)){
            playerType = "white";
        } else if(gameData.blackUsername()!=null && gameData.blackUsername().equals(username)){
            playerType = "black";
        } else{
            playerType = "observer";
        }

        String message = username + " joined game " + gameID + " as " + playerType;
        NotificationMessage connectMessage = new NotificationMessage(NOTIFICATION, message);
        connections.broadcast(gameID, username, connectMessage);

        //send load game message back to the user
        LoadGameMessage gameMessage = new LoadGameMessage(LOAD_GAME, game);
        connections.sendMessage(session, gameMessage);
        System.out.println("just sent message");
    }

    private void makeMove(Session session, String username, String message) throws IOException, DataAccessException, InvalidMoveException {
        MakeMoveCommand moveCommand = gson.fromJson(message, MakeMoveCommand.class);
        ChessMove move = moveCommand.getChessMove();

        int gameID = moveCommand.getGameID();
        GameData gameData = getValidGameData(session, gameID);
        if (gameData == null) {return;}

        ChessGame game = gameData.game();

        if(game.isGameOver()){
            connections.sendMessage(session, new ErrorMessage(ERROR, "Error: someone has resigned, can no longer make moves"));
            return;
        }

        ChessBoard board = game.getBoard();
        ChessGame.TeamColor currTurnColor = game.getTeamTurn();
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();

        //checks if current user is moving its own piece on its own turn
        if(currTurnColor == getPlayerColor(username, gameData)){
            System.out.println(currTurnColor + " player wants to make a move, on its turn (correct)");
            ChessPiece startPiece = board.getPiece(startPos);

            if(startPiece.getTeamColor()== currTurnColor){
                System.out.println("player is correctly requesting to move its own piece");
            }
            else{
                connections.sendMessage(session, new ErrorMessage(ERROR, "Error: player cannot move a piece that's not its own"));
                return;
            }
        }
        else{
            connections.sendMessage(session, new ErrorMessage(ERROR, "Error: player acting out of turn"));
            return;
        }

        //checks if the requested move was a valid one
        Collection<ChessMove> validMoves = game.validMoves(startPos);
        boolean moveFound=false;
        for (ChessMove potentialMove: validMoves){
            ChessPosition potentialPosition = move.getEndPosition();
            if(potentialPosition.equals(endPos)){
                moveFound = true;
            }
        }
        if(moveFound){
            System.out.println("valid move found");
        }
        else{
            connections.sendMessage(session, new ErrorMessage(ERROR, "Error: not a valid move"));
            return;
        }

        //update game w move
        game.makeMove(move);
        GameData gameWithMoveMade = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
        sqlGameDataAccess.updateGame(gameWithMoveMade);

        //send load game message to all
        LoadGameMessage gameMessage = new LoadGameMessage(LOAD_GAME, game);
        connections.broadcast(gameID, "", gameMessage);

        //send notification message to all others saying what move was made
        String moveMessage = currTurnColor + " player " + username + " moved";
        NotificationMessage moveNotification = new NotificationMessage(NOTIFICATION, moveMessage);
        connections.broadcast(gameID, username, moveNotification);

        //send notification message to all if the move results in check, checkmate, or stalemate
        if(game.isInCheck(currTurnColor)){
            String checkMessage = currTurnColor + " player " + username + " is in check";
            NotificationMessage checkNotification = new NotificationMessage(NOTIFICATION, checkMessage);
            connections.broadcast(gameID, "", checkNotification);
        }

        if(game.isInCheckmate(currTurnColor)){
            String checkmateMessage = currTurnColor + " player " + username + " is in checkmate";
            NotificationMessage checkmateNotification = new NotificationMessage(NOTIFICATION, checkmateMessage);
            connections.broadcast(gameID, "", checkmateNotification);
            game.setGameOver(true);
        }

        if(game.isInStalemate(currTurnColor)){
            String stalemateMessage = currTurnColor + " player " + username + " is in stalemate";
            NotificationMessage stalemateNotification = new NotificationMessage(NOTIFICATION, stalemateMessage);
            connections.broadcast(gameID, "", stalemateNotification);
            game.setGameOver(true);
        }
    }

    private void leaveGame(Session session, String username, UserGameCommand command) throws DataAccessException, IOException {
        int gameID = command.getGameID();
        GameData originalGame = getValidGameData(session, gameID);
        if (originalGame == null) {return;}

        GameData updatedGame;
        NotificationMessage leaveMessage = new NotificationMessage(NOTIFICATION, String.format("%s left the game", username));

        if(originalGame.whiteUsername()!=null && originalGame.whiteUsername().equals(username)){
            updatedGame = new GameData(originalGame.gameID(), null, originalGame.blackUsername(), originalGame.gameName(), originalGame.game());
        } else if(originalGame.blackUsername()!=null && originalGame.blackUsername().equals(username)){
            updatedGame = new GameData(originalGame.gameID(), originalGame.whiteUsername(), null, originalGame.gameName(), originalGame.game());
        } else{
            updatedGame = originalGame;
        }

        connections.broadcast(gameID, username, leaveMessage);
        sqlGameDataAccess.updateGame(updatedGame);
        connections.remove(gameID, username);
    }

    private void resign(Session session, String username, UserGameCommand command) throws IOException, DataAccessException {
        int gameID = command.getGameID();
        GameData gameData = getValidGameData(session, gameID);
        if (gameData == null) {return;}

        if (getPlayerColor(username, gameData)!=null){
            System.out.println("valid resign request received from a player");
            ChessGame chessGame = gameData.game();
            GameData gameOver;
            if(!chessGame.isGameOver()){
                chessGame.setGameOver(true);
                NotificationMessage resignMessage = new NotificationMessage(NOTIFICATION, String.format("%s resigned from the game", username));
                connections.broadcast(gameID, "", resignMessage);

                gameOver = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), chessGame);
                sqlGameDataAccess.updateGame(gameOver);
            }
            else{
                connections.sendMessage(session, new ErrorMessage(ERROR, "Error: cannot resign from game that is already over"));
            }
        }
        else{
            connections.sendMessage(session, new ErrorMessage(ERROR, "Error: observer cannot resign from a game"));
        }
    }

    private ChessGame.TeamColor getPlayerColor(String username, GameData game) {
        if(game.whiteUsername()!=null && game.whiteUsername().equals(username)) {
            return WHITE;
        }
        else if(game.blackUsername()!=null && game.blackUsername().equals(username)) {
            return BLACK;
        }
        else{
            return null;
        }
    }

    private GameData getValidGameData(Session session, int gameID) throws DataAccessException, IOException {
        GameData gameData = sqlGameDataAccess.getGame(gameID);
        if (gameData == null) {
            connections.sendMessage(session, new ErrorMessage(ERROR, "Error: enter a valid game ID"));
            return null;
        }
        return gameData;
    }
}