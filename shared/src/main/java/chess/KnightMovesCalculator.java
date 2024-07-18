package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMovesCalculator {
    private final ChessGame.TeamColor pieceColor;
    private final ChessBoard board;

    public KnightMovesCalculator(ChessGame.TeamColor pieceColor, ChessBoard board) {
        this.pieceColor = pieceColor;
        this.board = board;
    }

    public Collection<ChessMove> pieceMove(ChessPosition position) {
        List<ChessMove> legalMoves = new ArrayList<>();

        int[][] moves = {{2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}, {2, -1}};

        return kingAndKnightMovements(position, legalMoves, moves, board, pieceColor);
    }

    static Collection<ChessMove> kingAndKnightMovements(ChessPosition position, List<ChessMove> legalMoves, int[][]
            moves, ChessBoard board, ChessGame.TeamColor pieceColor) {
        for (int[] move : moves) {
            int rowDirection = move[0];
            int colDirection = move[1];

            int currentRow = position.getRow() + rowDirection;
            int currentCol = position.getColumn() + colDirection;

            if (currentRow > 0 && currentRow <= 8 && currentCol > 0 && currentCol <= 8) {
                ChessPosition newPosition = new ChessPosition(currentRow, currentCol);
                ChessPiece newPositionPiece = board.getPiece(newPosition);


                if (newPositionPiece == null) {
                    legalMoves.add(new ChessMove(position, newPosition, null));
                } else {
                    if (newPositionPiece.getTeamColor() != pieceColor) {
                        legalMoves.add(new ChessMove(position, newPosition, null));
                    }
                }
            }
        }
        return legalMoves;
    }
}
