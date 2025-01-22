package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();

        ChessPiece myPiece = board.getPiece(position);
        ChessGame.TeamColor myColor = myPiece.getTeamColor();

        int[][] combinations = {
                {1,2},
                {1,-2},
                {-1,2},
                {-1,-2},
                {2,1},
                {2,-1},
                {-2,1},
                {-2,-1}
        };

        for (int i=0; i<combinations.length; i++){
            int newRow = row + combinations[i][0];
            int newCol = col + combinations[i][1];
            ChessPosition newPos = new ChessPosition(newRow, newCol);
            ChessMove currMove = new ChessMove(position, newPos, null);

            if(newRow>=1 && newRow<=8 && newCol>=1 && newCol<=8){
                if(board.getPiece(newPos) == null){
                    moves.add(currMove);
                }
                else{
                    ChessPiece newPiece = board.getPiece(newPos);
                    ChessGame.TeamColor newPieceColor = newPiece.getTeamColor();
                    if(newPieceColor != myColor){
                        moves.add(currMove);
                    }
                }
            }
        }
        return moves;
    }
}
