package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();

        ChessPiece myPiece = board.getPiece(position);
        ChessGame.TeamColor myColor = myPiece.getTeamColor();

        int[][] combinations = {
                {1,0},
                {-1,0},
                {0,1},
                {0,-1}
        };

        for (int i=0; i<combinations.length; i++){
            int newRow = row + combinations[i][0];
            int newCol = col + combinations[i][1];

            while(newRow>=1 && newRow<=8 && newCol>=1 && newCol<=8){
                ChessPosition newPos = new ChessPosition(newRow, newCol);
                ChessMove currMove = new ChessMove(position, newPos, null);

                if(board.getPiece(newPos) == null){
                    moves.add(currMove);
                }
                else{
                    ChessPiece newPiece = board.getPiece(newPos);
                    ChessGame.TeamColor newPieceColor = newPiece.getTeamColor();
                    if(newPieceColor != myColor){
                        moves.add(currMove);
                    }
                    break;
                }
                newRow += combinations[i][0];
                newCol += combinations[i][1];
            }
        }





        return moves;
    }
}
