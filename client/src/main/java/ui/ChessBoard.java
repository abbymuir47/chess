package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;

public class ChessBoard {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    // Padded characters.
    private static final String EMPTY = "  ";
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
        out.print(SET_BG_COLOR_LIGHT_GREY);
        String[] headers = { "a","b","c","d","e","f","g","h" };
        out.print(" ");
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);

            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
            }
        }
        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String letter) {
        out.print(SET_BG_COLOR_LIGHT_GREY);;
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(letter);

        setBlack(out);
    }

    private static void drawChessBoard(PrintStream out) {

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
                if (boardRow % 2 == 0) {
                    drawWhiteRowOfSquares(out);
                }
                else {
                    drawBlackRowOfSquares(out);
                }
        }
    }

    private static void drawWhiteRowOfSquares(PrintStream out) {
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            if (boardCol % 2 == 0){
                drawWhiteSquare(out, rand.nextBoolean() ? X : O);
            }else{
                drawBlackSquare(out, rand.nextBoolean() ? X : O);
            }
        }
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.println();
    }

    private static void drawBlackRowOfSquares(PrintStream out) {
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            if (boardCol % 2 == 0){
                drawBlackSquare(out, rand.nextBoolean() ? X : O);
            }else{
                drawWhiteSquare(out, rand.nextBoolean() ? X : O);
            }
        }
        out.print(SET_BG_COLOR_LIGHT_GREY);
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