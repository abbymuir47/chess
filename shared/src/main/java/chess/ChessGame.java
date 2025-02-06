package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static chess.ChessPiece.PieceType.*;

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

    public ChessGame(ChessGame original) {
        this.board = new ChessBoard(original.board);
        this.team = original.team;
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
        Collection<ChessMove> validMoves = new ArrayList<>();
        ChessPiece currPiece = board.getPiece(startPosition);

        if(currPiece ==null){
            return null;
        }
        else{
            //ChessPiece.PieceType type = currPiece.getPieceType();
            team = currPiece.getTeamColor();
            Collection<ChessMove> possibleMoves = currPiece.pieceMoves(board, startPosition);
            for (ChessMove move:possibleMoves){
                ChessPosition potentialPosition = move.getEndPosition();

                //make a copy of the chessgame
                ChessGame copy = new ChessGame(this);
                copy.board.addPiece(potentialPosition, currPiece);

                //how do i call the isInCheck function on the copy of the board, rather than on the board itself?
                if(!copy.isInCheck(team)){
                    validMoves.add(move);
                }
                copy = null;
            }
        }
        return validMoves;
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

        if (checkKnightAttack(teamColor, row, col)){
            return true;
        }
        if (checkKingAttack(teamColor, row, col)){
            return true;
        }
        if (checkRookAttack(teamColor, row, col)){
            return true;
        }
        if (checkBishopAttack(teamColor, row, col)){
            return true;
        }
        //doesn't need to check queen attack bc the queen is included when rook and bishop are checked

        if(teamColor == TeamColor.WHITE){
            if(row <8){
                int newRow = row + 1;
                return checkPawnAttack(teamColor, newRow, col);
            }
            else{
                return false;
            }
        }
        if(teamColor == TeamColor.BLACK){
            if(row >1){
                int newRow = row - 1;
                return checkPawnAttack(teamColor, newRow, col);
            }
            else{
                return false;
            }
        }
        return false;
    }

    private boolean checkPawnAttack(TeamColor teamColor, int newRow, int col) {
        //System.out.println("newRow:" + newRow);
        for (int i = -1; i < 2; i += 2) {
            //System.out.println("i:" + i);
            int newCol = col + i;
            //System.out.println("newCol:" + newCol);

            if (newCol >= 1 && newCol <= 8) {
                //System.out.println("in the if statement");
                ChessPosition attack = new ChessPosition(newRow, newCol);
                ChessPiece attackPiece = board.getPiece(attack);

                if (attackPiece != null) {
                    TeamColor newPieceColor = attackPiece.getTeamColor();
                    if (newPieceColor != teamColor) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkKnightAttack(TeamColor teamColor, int row, int col){
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
        return checkJumpAttack(teamColor, combinations, row, col, KNIGHT);
    }

    private boolean checkKingAttack(TeamColor teamColor, int row, int col){
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
        return checkJumpAttack(teamColor, combinations, row, col, KING);
    }
    private boolean checkRookAttack(TeamColor teamColor, int row, int col){
        int[][] combinations = {
                {1,0},
                {-1,0},
                {0,1},
                {0,-1}
        };
        return checkRecursiveAttack(teamColor, combinations, row, col, ROOK);
    }

    private boolean checkBishopAttack(TeamColor teamColor, int row, int col){
        int[][] combinations = {
                {1,1},
                {1,-1},
                {-1,1},
                {-1,-1}
        };
        return checkRecursiveAttack(teamColor, combinations, row, col, BISHOP);
    }


        private boolean checkJumpAttack(TeamColor teamColor, int[][] combinations, int row, int col, ChessPiece.PieceType type) {
            for (int i = 0; i < combinations.length; i++) {
                int newRow = row + combinations[i][0];
                int newCol = col + combinations[i][1];
                ChessPosition newPos = new ChessPosition(newRow, newCol);
                ChessPiece newPiece;

                if(newRow<1 || newRow>8 || newCol<1 || newCol>8){
                    continue;
                }
                else{
                    newPiece = board.getPiece(newPos);
                    if(newPiece != null){
                        TeamColor newPieceColor = newPiece.getTeamColor();
                        if(newPieceColor != teamColor){
                            if(newPiece.getPieceType() == type){
                                return true;
                            }
                        }
                    }
                    else{
                        continue;
                    }
                }
            }
            return false;
        }

        private boolean checkRecursiveAttack(TeamColor teamColor, int[][] combinations, int row, int col, ChessPiece.PieceType type){
            for (int i=0; i<combinations.length; i++){
                int newRow = row + combinations[i][0];
                int newCol = col + combinations[i][1];
                while(newRow>=1 && newRow<=8 && newCol>=1 && newCol<=8){
                    ChessPosition newPos = new ChessPosition(newRow, newCol);
                    if(board.getPiece(newPos) != null){
                        ChessPiece newPiece = board.getPiece(newPos);
                        ChessGame.TeamColor newPieceColor = newPiece.getTeamColor();
                        if(newPieceColor != teamColor){
                            if(newPiece.getPieceType() == type || newPiece.getPieceType() == QUEEN){
                                return true;
                            }
                        }
                        else{
                            break;
                        }
                    }
                    newRow += combinations[i][0];
                    newCol += combinations[i][1];
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