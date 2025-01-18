package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessPiece.PieceType.KING;
import static chess.ChessPiece.PieceType.PAWN;

public class KingMovesCalculator implements PieceMovesCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();

        /*
        [0][1]
        [0][-1]
        [1][0]
        [-1][0]
        [1][1]
        [1][-1]
        [-1][1]
        [-1][-1]
        */

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
            moves.add(new ChessMove(position, endPosition,KING));
        }
        return moves;
    }
}
