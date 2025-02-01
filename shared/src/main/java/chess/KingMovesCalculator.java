package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.*;
import static chess.ChessPiece.PieceType.KING;
import static chess.ChessPiece.PieceType.PAWN;

public class KingMovesCalculator implements PieceMovesCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {

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
        return jumpMoves(board, position, combinations);
    }
}
