package ui;

public class GameplayUI {
    private static final String[] PIECES = {
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.WHITE_ROOK, EscapeSequences.WHITE_KNIGHT, EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_QUEEN,
            EscapeSequences.WHITE_KING, EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_KNIGHT, EscapeSequences.WHITE_ROOK,
            EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN,
            EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN,
            EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN,
            EscapeSequences.BLACK_ROOK, EscapeSequences.BLACK_KNIGHT, EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_QUEEN,
            EscapeSequences.BLACK_KING, EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_KNIGHT, EscapeSequences.BLACK_ROOK,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY
    };

    private static final String[] PIECES_FLIPPED = {
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.BLACK_ROOK, EscapeSequences.BLACK_KNIGHT, EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_QUEEN,
            EscapeSequences.BLACK_KING, EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_KNIGHT, EscapeSequences.BLACK_ROOK,
            EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN,
            EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN,
            EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN,
            EscapeSequences.WHITE_ROOK, EscapeSequences.WHITE_KNIGHT, EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_QUEEN,
            EscapeSequences.WHITE_KING, EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_KNIGHT, EscapeSequences.WHITE_ROOK,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY
    };

    public static void drawBoard() {
        System.out.println("White at bottom:");
        drawBoardWhiteBottom();

        System.out.println("Black at bottom:");
        drawBoardBlackBottom();

    }

    private static void drawBoardWhiteBottom() {
        String borderColor = EscapeSequences.SET_BG_COLOR_BLACK;
        String[] boardColors = { EscapeSequences.SET_BG_COLOR_LIGHT_GREY, EscapeSequences.SET_BG_COLOR_DARK_GREY };
        String columnLabels = " hgfedcba";

        boardSetup(GameplayUI.PIECES_FLIPPED, borderColor, boardColors, columnLabels, "1", "2", "3", "4", "5", "6", "7", "8");

    }

    private static void drawBoardBlackBottom(){
        String borderColor = EscapeSequences.SET_BG_COLOR_BLACK;
        String[] boardColors = { EscapeSequences.SET_BG_COLOR_LIGHT_GREY, EscapeSequences.SET_BG_COLOR_DARK_GREY };
        String columnLabels = " abcdefgh";
        boardSetup(GameplayUI.PIECES, borderColor, boardColors, columnLabels, "8", "7", "6", "5",
                "4", "3", "2", "1");

    }

    private static void boardSetup(String[] pieces, String borderColor, String[] boardColors, String columnLabels,
                                   String number, String number2, String number3, String number4, String number5,
                                   String number6, String number7, String number8) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                // Border handling with row numbers and column letters
                if (row == 0 || row == 9) {
                    if (col == 0 || col == 9) {
                        System.out.print(borderColor + "   ");
                    } else {
                        System.out.print(borderColor + " " + columnLabels.charAt(col) + " ");
                    }
                } else if (col == 0 || col == 9) {
                    System.out.print(borderColor + " " + (row == 1 ? number : row == 2 ? number2 : row == 3 ? number3 :
                            row == 4 ? number4 : row == 5 ? number5 : row == 6 ? number6 : row == 7 ? number7 :
                                    row == 8 ? number8 : " ") + " ");
                } else {
                    int pieceIndex = (row ) * 8 + (col + 1);
                    String piece = pieces[pieceIndex];
                    System.out.print(boardColors[(row + col) % 2] + piece + EscapeSequences.RESET_TEXT_COLOR);
                }
            }
            System.out.print(EscapeSequences.RESET_BG_COLOR);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        drawBoard();
    }
}