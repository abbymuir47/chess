package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{

    private ChessGame game;
    private final String message;

    public LoadGameMessage(ServerMessageType type, String message, ChessGame game){
        super(type);
        this.message = message;
        this.game = game;
    }

    public ChessGame getGame(){return game;}

    public String getMessage(){return message;}
}