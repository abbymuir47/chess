package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.*;
import static chess.ChessPiece.PieceType.KING;
import static chess.ChessPiece.PieceType.PAWN;

public class KingMovesCalculator implements PieceMovesCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();

        //defines my current piece and gets its color
        ChessPiece myPiece = board.getPiece(position);
        ChessGame.TeamColor pieceColor = myPiece.getTeamColor();

        int[][] combinations = {
                {0,1},
                {0,-1},
                {1,0},
                {-1,0},
                {1,1},
                {1,-1},
                {-1,1},
                {-1,-1}
        };

        for (int i = 0; i < combinations.length; i++) {
            int newRow = row + combinations[i][0];
            int newCol = col + combinations[i][1];
            ChessPosition currMove = new ChessPosition(newRow, newCol);

            if(newRow<1 | newRow>8 | newCol<1 | newCol>8){
                continue;
            }
            else{
                ChessPiece newPos = board.getPiece(currMove);
                if(newPos != null){
                    ChessGame.TeamColor newSquareColor = newPos.getTeamColor();
                    if(newSquareColor != pieceColor){
                        moves.add(new ChessMove(position, currMove, null));
                    }
                }
                else{
                    moves.add(new ChessMove(position, currMove, null));
                }
            }
        }
        return moves;
    }
}
