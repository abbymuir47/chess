package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static chess.ChessGame.TeamColor.*;
import static chess.ChessPiece.PieceType.*;
import static ui.EscapeSequences.*;

public class ChessBoard {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;

    private static chess.ChessBoard currBoard;
    private static ColorPerspective currPerspective;
    private static PrintStream out;

    enum ColorPerspective{
        WHITE_PLAYER,
        BLACK_PLAYER
    }

    public ChessBoard(PrintStream out, chess.ChessBoard importedBoard, ColorPerspective playerColor) {
        this.out = out;
        this.currBoard = importedBoard;
        this.currPerspective = playerColor;
    }

    public static void drawBoard() {
        out.print(ERASE_SCREEN);

        drawHeaders(out, currPerspective);
        drawChessBoard(out, currPerspective);
        drawHeaders(out, currPerspective);
    }

    private static void drawHeaders(PrintStream out, ColorPerspective perspective) {
        out.print(SET_BG_COLOR_DARK_GREEN);
        out.print("   ");
        String[] headers = perspective == ColorPerspective.WHITE_PLAYER ?
                new String[]{"a", "b", "c", "d", "e", "f", "g", "h"} : new String[]{ "h","g","f","e","d","c","b","a" };
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawLetter(out, headers[boardCol]);
        }
        out.print("   ");
        setBlack(out);
        out.println();
    }

    private static void drawLetter(PrintStream out, String headerText) {
        out.print(SET_TEXT_COLOR_GREEN);
        out.print(" ");
        out.print(headerText);
        out.print(" ");
    }

    private static void drawChessBoard(PrintStream out, ColorPerspective perspective) {
        int startRow = perspective == ColorPerspective.WHITE_PLAYER ? 8 : 1;
        int endRow = perspective == ColorPerspective.WHITE_PLAYER ? 1 : 8;
        int step = perspective == ColorPerspective.WHITE_PLAYER ? -1 : 1;

        for (int boardRow = startRow; (perspective == ColorPerspective.WHITE_PLAYER ? boardRow >= endRow : boardRow <= endRow); boardRow += step) {
            if (boardRow % 2 == 0) {
                drawWhiteRowOfSquares(out, boardRow);
            }
            else {
                drawBlackRowOfSquares(out, boardRow);
            }
        }
    }

    private static void drawRowNumberSquare(PrintStream out, int boardRow) {
        out.print(SET_BG_COLOR_DARK_GREEN);
        out.print(SET_TEXT_COLOR_GREEN);
        out.print(" " + boardRow + " ");
    }

    private static void drawWhiteRowOfSquares(PrintStream out, int boardRow) {
        drawRowNumberSquare(out, boardRow);
        for (int boardCol = 8; boardCol > 0; --boardCol) {
            if (boardCol % 2 == 0){
                drawWhiteSquare(out, boardRow, boardCol);
            }else{
                drawBlackSquare(out, boardRow, boardCol);
            }
        }
        drawRowNumberSquare(out, boardRow);
        setBlack(out);
        out.println();
    }

    private static void drawBlackRowOfSquares(PrintStream out, int boardRow) {
        drawRowNumberSquare(out, boardRow);
        for (int boardCol = 8; boardCol > 0; --boardCol) {
            if (boardCol % 2 == 0){
                drawBlackSquare(out, boardRow, boardCol);
            }else{
                drawWhiteSquare(out, boardRow, boardCol);
            }
        }
        drawRowNumberSquare(out, boardRow);
        setBlack(out);
        out.println();
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void drawWhiteSquare(PrintStream out, int boardRow, int boardCol) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        drawPieces(out, boardRow, boardCol);
    }

    private static void drawBlackSquare(PrintStream out, int boardRow, int boardCol) {
        out.print(SET_BG_COLOR_DARK_GREY);
        drawPieces(out, boardRow, boardCol);
    }

    private static void drawPieces(PrintStream out, int boardRow, int boardCol) {
        ChessPosition currPos = new ChessPosition(boardRow, boardCol);
        ChessPiece currPiece = currBoard.getPiece(currPos);
        if(currPiece == null){
            out.print("   ");
        }
        else{
            out.print(" ");
            if (currPiece.getTeamColor() == WHITE){
                out.print(SET_TEXT_COLOR_WHITE);
                drawPieceTypes(out, currPiece);
            }
            else{
                out.print(SET_TEXT_COLOR_BLACK);
                drawPieceTypes(out, currPiece);
            }
            out.print(" ");
        }
    }

    private static void drawPieceTypes(PrintStream out, ChessPiece currPiece) {
        if(currPiece.getPieceType() == PAWN){
            out.print("P");}
        else if(currPiece.getPieceType() == ROOK){
            out.print("R");}
        else if(currPiece.getPieceType() == KNIGHT){
            out.print("N");}
        else if(currPiece.getPieceType() == KING){
            out.print("K");}
        else if(currPiece.getPieceType() == QUEEN){
            out.print("Q");}
        else if(currPiece.getPieceType() == BISHOP){
            out.print("B");}
    }
}