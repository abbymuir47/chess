package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //return new ArrayList<>();
        //implement KingMovesCalculator,... classes using a switch statement in the ChessPiece class
        PieceMovesCalculator moves = switch(getPieceType()){
            case PAWN -> new PawnMovesCalculator();
            case ROOK -> new RookMovesCalculator();
            case KNIGHT -> new KnightMovesCalculator();
            case BISHOP -> new BishopMovesCalculator();
            case KING -> new KingMovesCalculator();
            case QUEEN -> new QueenMovesCalculator();
        };
        return moves.pieceMoves(board, myPosition);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        if(pieceColor== ChessGame.TeamColor.WHITE){
            if(type == PieceType.PAWN){
                return "P";
            }
            if(type == PieceType.QUEEN){
                return "Q";
            }
            if(type == PieceType.KING){
                return "K";
            }
            if(type == PieceType.KNIGHT){
                return "N";
            }
            if(type == PieceType.ROOK){
                return "R";
            }
            if(type == PieceType.BISHOP){
                return "B";
            }
        }
        else if(pieceColor== ChessGame.TeamColor.BLACK){
            if(type == PieceType.PAWN){
                return "p";
            }
            if(type == PieceType.QUEEN){
                return "q";
            }
            if(type == PieceType.KING){
                return "k";
            }
            if(type == PieceType.KNIGHT){
                return "n";
            }
            if(type == PieceType.ROOK){
                return "r";
            }
            if(type == PieceType.BISHOP){
                return "b";
            }
        }

        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}