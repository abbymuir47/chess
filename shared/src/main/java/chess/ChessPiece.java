package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

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

        ChessPiece piece = board.getPiece(myPosition);
        type = piece.getPieceType();

        switch (type) {
            case PAWN:
                PawnMovesCalculator pawnMoves = new PawnMovesCalculator();
                return pawnMoves.pieceMoves(board, myPosition);
            case ROOK:
                RookMovesCalculator rookMoves = new RookMovesCalculator();
                return rookMoves.pieceMoves(board, myPosition);
            case KNIGHT:
                KnightMovesCalculator knightMoves = new KnightMovesCalculator();
                return knightMoves.pieceMoves(board, myPosition);
            case BISHOP:
                BishopMovesCalculator bishopMoves = new BishopMovesCalculator();
                return bishopMoves.pieceMoves(board, myPosition);
            case KING:
                KingMovesCalculator kingMoves = new KingMovesCalculator();
                return kingMoves.pieceMoves(board, myPosition);
            case QUEEN:
                QueenMovesCalculator queenMoves = new QueenMovesCalculator();
                return queenMoves.pieceMoves(board, myPosition);
            default:
                throw new IllegalArgumentException();
        }
    }
}
