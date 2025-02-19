package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.*;
import static chess.ChessPiece.PieceType.*;

public class PawnMovesCalculator implements PieceMovesCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();

        //defines my current piece and gets its color
        ChessPiece myPiece = board.getPiece(position);
        ChessGame.TeamColor myColor = myPiece.getTeamColor();

        //code for moving black pawns
        if(myColor == BLACK){
            validateMoves(board, position, moves, myColor, -1, 7, 2);
        }
        //code for moving white pawns
        else if(myColor == WHITE){
            validateMoves(board, position, moves, myColor, 1, 2, 7);
        }
        return moves;
    }

    private static void validateMoves(
            ChessBoard board, ChessPosition position, Collection<ChessMove> moves,
            ChessGame.TeamColor myColor, int forward, int startRow, int endRow) {
        int row = position.getRow();
        int col = position.getColumn();

        ChessPosition forwardMove = new ChessPosition(row + forward, col);
        ChessPosition attackRight = new ChessPosition(row + forward, col +1);
        ChessPosition attackLeft = new ChessPosition(row + forward, col -1);

        //checks ability to move forward
        checkForward(board, position, moves, forward, startRow, endRow, forwardMove, row, col);
        //checks if piece can capture a piece diagonally to the right
        checkAttack(col < 8, board, attackRight, myColor, moves, position, endRow);
        //checks if piece can capture a piece diagonally to the left
        checkAttack(col > 1, board, attackLeft, myColor, moves, position, endRow);
    }

    private static void checkForward(ChessBoard board, ChessPosition position, Collection<ChessMove> moves,
                                     int forward, int startRow, int endRow,
                                     ChessPosition forwardMove, int row, int col) {
        if(board.getPiece(forwardMove) == null){
            if (row == endRow){
                promote(position, moves, forwardMove);
            }
            else{
                moves.add(new ChessMove(position, forwardMove, null));
                //checks if the piece is at its starting position and can move forward 2 spaces
                if (row == startRow) {
                    ChessPosition moveTwo = new ChessPosition(row + (2* forward), col);
                    if (board.getPiece(moveTwo) == null) {
                        moves.add(new ChessMove(position, moveTwo, null));
                    }
                }
            }
        }
    }

    private static void checkAttack(boolean col, ChessBoard board, ChessPosition newPos,
                                    ChessGame.TeamColor myColor, Collection<ChessMove> moves,
                                    ChessPosition position, int endRow) {
        if (col) {
            ChessPiece newPiece = board.getPiece(newPos);
            if (newPiece != null && newPiece.getTeamColor() != myColor) {
                if(position.getRow() != endRow) {
                    moves.add(new ChessMove(position, newPos, null));
                }
                else{
                    promote(position, moves, newPos);
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