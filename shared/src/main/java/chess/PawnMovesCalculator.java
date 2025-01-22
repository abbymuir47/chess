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
            ChessPosition forwardMove = new ChessPosition(row-1,col);
            ChessPosition attackRight = new ChessPosition(row-1,col+1);
            ChessPosition attackLeft = new ChessPosition(row-1,col-1);

            if (row > 2) {
                //moves forward 1 space
                if(board.getPiece(forwardMove) == null){
                    moves.add(new ChessMove(position, forwardMove, null));
                    //checks if the piece is at its starting position and can move forward 2 spaces
                    if (row == 7) {
                        ChessPosition moveTwo = new ChessPosition(row - 2, col);
                        if (board.getPiece(moveTwo) == null) {
                            moves.add(new ChessMove(position, moveTwo, null));
                        }
                    }
                }
                //checks if piece can capture a piece diagonally to the right
                if(col<8){
                    ChessPiece newPiece = board.getPiece(attackRight);
                    if(newPiece != null && newPiece.getTeamColor() == WHITE){
                        moves.add(new ChessMove(position, attackRight, null));
                    }
                }
                //checks if piece can capture a piece diagonally to the left
                if(col>1){
                    ChessPiece newPiece = board.getPiece(attackLeft);
                    if(newPiece != null && newPiece.getTeamColor() == WHITE){
                        moves.add(new ChessMove(position, attackLeft, null));
                    }
                }
            }
            //checks for promotion
            else if (row == 2){
                //moves forward 1 space
                if(board.getPiece(forwardMove) == null){
                    moves.add(new ChessMove(position, forwardMove, ROOK));
                    moves.add(new ChessMove(position, forwardMove, BISHOP));
                    moves.add(new ChessMove(position, forwardMove, KNIGHT));
                    moves.add(new ChessMove(position, forwardMove, QUEEN));
                }
                //checks if piece can capture a piece diagonally to the right
                if(col < 8){
                    ChessPiece newPiece = board.getPiece(attackRight);
                    if(newPiece != null && newPiece.getTeamColor() == WHITE){
                        moves.add(new ChessMove(position, attackRight, ROOK));
                        moves.add(new ChessMove(position, attackRight, BISHOP));
                        moves.add(new ChessMove(position, attackRight, KNIGHT));
                        moves.add(new ChessMove(position, attackRight, QUEEN));
                    }
                }
                //checks if piece can capture a piece diagonally to the left
                if(col > 1){
                    ChessPiece newPiece = board.getPiece(attackLeft);
                    if(newPiece != null && newPiece.getTeamColor() == WHITE){
                        moves.add(new ChessMove(position, attackLeft, ROOK));
                        moves.add(new ChessMove(position, attackLeft, BISHOP));
                        moves.add(new ChessMove(position, attackLeft, KNIGHT));
                        moves.add(new ChessMove(position, attackLeft, QUEEN));
                    }
                }
            }
        }
        //code for moving white pawns
        else if(myColor == WHITE){
            ChessPosition forwardMove = new ChessPosition(row+1,col);
            ChessPosition attackRight = new ChessPosition(row+1,col+1);
            ChessPosition attackLeft = new ChessPosition(row+1,col-1);

            if (row < 7) {
                //moves forward 1 space
                if(board.getPiece(forwardMove) == null){
                    moves.add(new ChessMove(position, forwardMove, null));
                    //checks if the piece is at its starting position and can move forward 2 spaces
                    if (row == 2) {
                        ChessPosition moveTwo = new ChessPosition(row + 2, col);
                        if (board.getPiece(moveTwo) == null) {
                            moves.add(new ChessMove(position, moveTwo, null));
                        }
                    }
                }
                //checks if piece can capture a piece diagonally to the right
                if(col<8){
                    ChessPiece newPiece = board.getPiece(attackRight);
                    if(newPiece != null && newPiece.getTeamColor() == BLACK){
                        moves.add(new ChessMove(position, attackRight, null));
                    }
                }
                //checks if piece can capture a piece diagonally to the left
                if(col>1){
                    ChessPiece newPiece = board.getPiece(attackLeft);
                    if(newPiece != null && newPiece.getTeamColor() == BLACK){
                        moves.add(new ChessMove(position, attackLeft, null));
                    }
                }
            }
            //checks for promotion
            else if (row == 7){
                //moves forward 1 space
                if(board.getPiece(forwardMove) == null){
                    moves.add(new ChessMove(position, forwardMove, ROOK));
                    moves.add(new ChessMove(position, forwardMove, BISHOP));
                    moves.add(new ChessMove(position, forwardMove, KNIGHT));
                    moves.add(new ChessMove(position, forwardMove, QUEEN));
                }
                //checks if piece can capture a piece diagonally to the right
                if(col < 8){
                    ChessPiece newPiece = board.getPiece(attackRight);
                    if(newPiece != null && newPiece.getTeamColor() == BLACK){
                        moves.add(new ChessMove(position, attackRight, ROOK));
                        moves.add(new ChessMove(position, attackRight, BISHOP));
                        moves.add(new ChessMove(position, attackRight, KNIGHT));
                        moves.add(new ChessMove(position, attackRight, QUEEN));
                    }
                }
                //checks if piece can capture a piece diagonally to the left
                if(col > 1){
                    ChessPiece newPiece = board.getPiece(attackLeft);
                    if(newPiece != null && newPiece.getTeamColor() == BLACK){
                        moves.add(new ChessMove(position, attackLeft, ROOK));
                        moves.add(new ChessMove(position, attackLeft, BISHOP));
                        moves.add(new ChessMove(position, attackLeft, KNIGHT));
                        moves.add(new ChessMove(position, attackLeft, QUEEN));
                    }
                }
            }
        }
        return moves;
    }
}
