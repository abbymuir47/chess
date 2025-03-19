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

    // Padded characters.
    private static final String EMPTY = " ";
    private static final String X = " X ";
    private static final String O = " O ";

    private static Random rand = new Random();
    private static chess.ChessBoard exampleBoard = new chess.ChessBoard();

    public static void main(String[] args) {
        exampleBoard.resetBoard();

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out);
        drawChessBoard(out);
        drawHeaders(out);
    }

    private static void drawHeaders(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREEN);
        out.print("   ");
        String[] headers = { "a","b","c","d","e","f","g","h" };
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

    private static void drawChessBoard(PrintStream out) {
        for (int boardRow = 8; boardRow > 0; --boardRow) {
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

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
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
        ChessPiece currPiece = exampleBoard.getPiece(currPos);
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