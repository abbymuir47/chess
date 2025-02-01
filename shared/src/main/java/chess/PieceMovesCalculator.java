package chess;
import java.util.ArrayList;
import java.util.Collection;

public interface PieceMovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);


    default Collection<ChessMove> recursiveMoves(ChessBoard board, ChessPosition position, int [][] combinations){
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();

        ChessPiece myPiece = board.getPiece(position);
        ChessGame.TeamColor myColor = myPiece.getTeamColor();

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


    default Collection<ChessMove> jumpMoves(ChessBoard board, ChessPosition position, int [][] combinations) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();

        //defines my current piece and gets its color
        ChessPiece myPiece = board.getPiece(position);
        ChessGame.TeamColor pieceColor = myPiece.getTeamColor();

        for (int i = 0; i < combinations.length; i++) {
            int newRow = row + combinations[i][0];
            int newCol = col + combinations[i][1];
            ChessPosition newPos = new ChessPosition(newRow, newCol);

            if(newRow<1 | newRow>8 | newCol<1 | newCol>8){
                continue;
            }
            else{
                ChessPiece newPiece = board.getPiece(newPos);
                if(newPiece != null){
                    ChessGame.TeamColor newPieceColor = newPiece.getTeamColor();
                    if(newPieceColor != pieceColor){
                        moves.add(new ChessMove(position, newPos, null));
                    }
                }
                else{
                    moves.add(new ChessMove(position, newPos, null));
                }
            }
        }
        return moves;
    }

}
