package chess;

import java.util.Collection;
import java.util.Objects;

import static chess.ChessPiece.PieceType.KING;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private ChessGame.TeamColor team;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        team = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return team;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    /**Takes as input a position on the chessboard and returns all moves the piece there can legally make.
     * If there is no piece at that location, this method returns null.
     * A move is valid if it is a "piece move" for the piece at the input location and
     * making that move would not leave the team’s king in danger of check.
    */
     public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        throw new RuntimeException("Not implemented");
        /*
        if(board.getPiece(startPosition)==null){
            return null;
        }
        else{}
         */
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     * Receives a given move and executes it, provided it is a legal move.
     * If the move is illegal, it throws an InvalidMoveException.
     * A move is illegal if it is not a "valid" move for the piece at the starting location,
     * or if it’s not the corresponding team's turn.
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     * Returns true if the specified team’s King could be captured by an opposing piece.
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition;
        kingPosition = getKingPosition(teamColor);
        int row = kingPosition.getRow();
        int col = kingPosition.getColumn();

        if (checkJumpAttack(teamColor, row, col)){
            return true;
        }
        if (checkRecursiveAttack(teamColor, row, col)){
            return true;
        }
        if(teamColor == TeamColor.WHITE){
            int newRow = row + 1;
            return checkPawnAttack(teamColor, newRow, col);
        }
        else if(teamColor == TeamColor.BLACK){
            int newRow = row - 1;
            return checkPawnAttack(teamColor, newRow, col);
        }
        else{
            return false;
        }
    }

    private boolean checkPawnAttack(TeamColor teamColor, int newRow, int col) {
        for(int i=-1; i<2; i+=2){
            if(col+i >0 && col+i <9){
                ChessPosition attack = new ChessPosition(newRow, col +i);
                ChessPiece attackPiece = board.getPiece(attack);
                if(attackPiece != null){
                    TeamColor newPieceColor = attackPiece.getTeamColor();
                    if(newPieceColor != teamColor){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkJumpAttack(TeamColor teamColor, int row, int col) {
        int[][] combinations = {
                //possible opposing knight positions
                {1,2}, {1,-2}, {-1,2}, {-1,-2}, {2,1}, {2,-1}, {-2,1}, {-2,-1},
                //possible opposing king positions
                {0,1}, {0,-1}, {1,0}, {-1,0}, {1,1}, {1,-1}, {-1,1}, {-1,-1}
        };
        for (int i = 0; i < combinations.length; i++) {
            int newRow = row + combinations[i][0];
            int newCol = col + combinations[i][1];
            ChessPosition newPos = new ChessPosition(newRow, newCol);
            ChessPiece newPiece;

            if(newRow<1 | newRow>8 | newCol<1 | newCol>8){
                continue;
            }
            else{
                newPiece = board.getPiece(newPos);
                if(newPiece != null){
                    TeamColor newPieceColor = newPiece.getTeamColor();
                    if(newPieceColor != teamColor){
                        return true;
                    }
                }
                else{
                    continue;
                }
            }
        }
        return false;
    }

    private boolean checkRecursiveAttack(TeamColor teamColor, int row, int col){
        int[][] combinations = {
                //possible opposing rook positions
                {1,0}, {-1,0}, {0,1}, {0,-1},
                //possible opposing bishop positions
                {1,1}, {1,-1}, {-1,1}, {-1,-1}
        };

        for (int i=0; i<combinations.length; i++){
            int newRow = row + combinations[i][0];
            int newCol = col + combinations[i][1];

            while(newRow>=1 && newRow<=8 && newCol>=1 && newCol<=8){
                ChessPosition newPos = new ChessPosition(newRow, newCol);
                if(board.getPiece(newPos) != null){
                    ChessPiece newPiece = board.getPiece(newPos);
                    ChessGame.TeamColor newPieceColor = newPiece.getTeamColor();
                    if(newPieceColor != teamColor){
                        return true;
                    }
                    else{
                        break;
                    }
                }
                else{
                    newRow += combinations[i][0];
                    newCol += combinations[i][1];
                }
            }
        }
        return false;
    }

    private ChessPosition getKingPosition(TeamColor teamColor) {
        ChessPosition currPos;
        ChessPiece currPiece;
        for(int i = 1; i<9; i++){
            for(int j=1;j<9;j++){
                currPos = new ChessPosition(i,j);
                currPiece = board.getPiece(currPos);
                if (currPiece != null && currPiece.getTeamColor() == teamColor){
                    if(currPiece.getPieceType() == KING){
                        return currPos;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     * Returns true if the given team has no way to protect their king from being captured
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     * Returns true if the given team has no legal moves but their king is not in immediate danger.
     * can't make any moves that wouldn't put your king in check
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && team == chessGame.team;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, team);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", team=" + team +
                '}';
    }
}
