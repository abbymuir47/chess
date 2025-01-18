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

        //get coordinates for moving one square forward or attacking one square diagonally
        ChessPosition forwardMove;
        ChessPosition attackOne;
        ChessPosition attackTwo;

        if(pieceColor == BLACK){
            forwardMove = new ChessPosition(row-1,col);
            attackOne = new ChessPosition(row-1, col+1);
            attackTwo = new ChessPosition(row-1, col-1);
        } else { //if the color is white
            forwardMove = new ChessPosition(row+1,col);
            attackOne = new ChessPosition(row+1, col+1);
            attackTwo = new ChessPosition(row+1, col-1);
        }

        ChessPiece checkPiece = board.getPiece(forwardMove);
        if(checkPiece != null){
            moves.add(new ChessMove(position, forwardMove,PAWN));
        }

        ChessPiece checkAttackOne = board.getPiece(attackOne);
        if(checkAttackOne != null){
            ChessGame.TeamColor attackSquareColor = checkAttackOne.getTeamColor();
            if(attackSquareColor != pieceColor){
                moves.add(new ChessMove(position, attackOne,PAWN));
            }
        }

        ChessPiece checkAttackTwo = board.getPiece(attackTwo);
        if(checkAttackTwo != null){
            ChessGame.TeamColor attackSquareColor = checkAttackTwo.getTeamColor();
            if(attackSquareColor != pieceColor){
                moves.add(new ChessMove(position, attackTwo,PAWN));
            }
        }

        return moves;
    }
}
