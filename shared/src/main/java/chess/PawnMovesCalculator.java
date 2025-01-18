package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.*;
import static chess.ChessPiece.PieceType.PAWN;

public class PawnMovesCalculator implements PieceMovesCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();

        //defines my current piece and gets its color
        ChessPiece myPiece = board.getPiece(position);
        ChessGame.TeamColor pieceColor = myPiece.getTeamColor();

        ChessPosition endPosition;
        if(pieceColor == BLACK){
            endPosition = new ChessPosition(row-1,col);
        } else { //if the color is white
            endPosition = new ChessPosition(row+1,col);
        }

        ChessPiece checkPiece = board.getPiece(endPosition);
        if(checkPiece != null){
            moves.add(new ChessMove(position, endPosition,PAWN));
        }
        return moves;
    }
}
