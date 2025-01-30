package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.*;
import static chess.ChessPiece.PieceType.*;

public class PawnMovesCalculator implements PieceMovesCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();

        //defines my current piece and gets its color
        ChessPiece myPiece = board.getPiece(position);
        ChessGame.TeamColor myColor = myPiece.getTeamColor();

        //code for moving black pawns
        if(myColor == BLACK){
            validateMoves(board, position, row, col, moves, myColor, -1, 7, 2);
        }
        //code for moving white pawns
        else if(myColor == WHITE){
            validateMoves(board, position, row, col, moves, myColor, 1, 2, 7);
        }
        return moves;
    }

    private static void validateMoves(ChessBoard board, ChessPosition position, int row, int col, Collection<ChessMove> moves, ChessGame.TeamColor myColor, int forward, int startRow, int endRow) {
        ChessPosition forwardMove = new ChessPosition(row + forward, col);
        ChessPosition attackRight = new ChessPosition(row + forward, col +1);
        ChessPosition attackLeft = new ChessPosition(row + forward, col -1);

        if (row != endRow) {
            //moves forward 1 space
            if(board.getPiece(forwardMove) == null){
                moves.add(new ChessMove(position, forwardMove, null));
                //checks if the piece is at its starting position and can move forward 2 spaces
                if (row == startRow) {
                    ChessPosition moveTwo = new ChessPosition(row + (2*forward), col);
                    if (board.getPiece(moveTwo) == null) {
                        moves.add(new ChessMove(position, moveTwo, null));
                    }
                }
            }
            //checks if piece can capture a piece diagonally to the right
            if(col <8){
                ChessPiece newPiece = board.getPiece(attackRight);
                if(newPiece != null && newPiece.getTeamColor() != myColor){
                    moves.add(new ChessMove(position, attackRight, null));
                }
            }
            //checks if piece can capture a piece diagonally to the left
            if(col >1){
                ChessPiece newPiece = board.getPiece(attackLeft);
                if(newPiece != null && newPiece.getTeamColor() != myColor){
                    moves.add(new ChessMove(position, attackLeft, null));
                }
            }
        }
        //checks for promotion
        else if (row == endRow){
            //moves forward 1 space
            if(board.getPiece(forwardMove) == null){
                promote(position, moves, forwardMove);
            }
            //checks if piece can capture a piece diagonally to the right
            if(col < 8){
                ChessPiece newPiece = board.getPiece(attackRight);
                if(newPiece != null && newPiece.getTeamColor() != myColor){
                    promote(position, moves, attackRight);
                }
            }
            //checks if piece can capture a piece diagonally to the left
            if(col > 1){
                ChessPiece newPiece = board.getPiece(attackLeft);
                if(newPiece != null && newPiece.getTeamColor() != myColor){
                    promote(position, moves, attackLeft);
                }
            }
        }
    }

    private static void promote(ChessPosition position, Collection<ChessMove> moves, ChessPosition move) {
        moves.add(new ChessMove(position, move, ROOK));
        moves.add(new ChessMove(position, move, BISHOP));
        moves.add(new ChessMove(position, move, KNIGHT));
        moves.add(new ChessMove(position, move, QUEEN));
    }
}