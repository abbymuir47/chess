package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;

public class ChessBoard {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;

    // Padded characters.
    private static final String EMPTY = " ";
    private static final String X = " X ";
    private static final String O = " O ";

    private static Random rand = new Random();


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out);
        drawChessBoard(out);
    }

    private static void drawHeaders(PrintStream out) {
        out.print("   ");
        out.print(SET_BG_COLOR_LIGHT_GREY);
        String[] headers = { "a","b","c","d","e","f","g","h" };
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);
        }
        setBlack(out);
        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = 1;
        int suffixLength = 1;

        out.print(" ");
        printHeaderText(out, headerText);
        out.print(" ");
    }

    private static void printHeaderText(PrintStream out, String letter) {
        out.print(SET_BG_COLOR_LIGHT_GREY);;
        out.print(SET_TEXT_COLOR_GREEN);
        out.print(letter);
    }

    private static void drawChessBoard(PrintStream out) {
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            if (boardRow % 2 == 0) {
                drawWhiteRowOfSquares(out, boardRow);
            }
            else {
                drawBlackRowOfSquares(out, boardRow);
            }
        }
    }

    private static void drawRowNumberSquare(PrintStream out, int boardRow) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_GREEN);
        out.print(" " + boardRow + " ");
    }

    private static void drawWhiteRowOfSquares(PrintStream out, int boardRow) {
        drawRowNumberSquare(out, boardRow);
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            if (boardCol % 2 == 0){
                drawWhiteSquare(out, rand.nextBoolean() ? X : O);
            }else{
                drawBlackSquare(out, rand.nextBoolean() ? X : O);
            }
        }
        drawRowNumberSquare(out, boardRow);
        setBlack(out);
        out.println();
    }

    private static void drawBlackRowOfSquares(PrintStream out, int boardRow) {
        drawRowNumberSquare(out, boardRow);
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            if (boardCol % 2 == 0){
                drawBlackSquare(out, rand.nextBoolean() ? X : O);
            }else{
                drawWhiteSquare(out, rand.nextBoolean() ? X : O);
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

    private static void drawWhiteSquare(PrintStream out, String player) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print(player);
    }

    private static void drawBlackSquare(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print(player);
    }
}