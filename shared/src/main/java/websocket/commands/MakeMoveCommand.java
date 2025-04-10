package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{

    private final ChessMove move;
    private String startPos;
    private String endPos;

    public MakeMoveCommand(CommandType commandType, String authToken, Integer gameID, ChessMove move, String startPos, String endPos) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public ChessMove getChessMove() {
        return move;
    }

    public String getStartPos() {
        return startPos;
    }

    public String getEndPos() {
        return endPos;
    }
}
