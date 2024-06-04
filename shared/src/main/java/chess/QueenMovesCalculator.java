package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMovesCalculator {
    private final ChessGame.TeamColor pieceColor;
    private final ChessBoard board;

    public QueenMovesCalculator(ChessGame.TeamColor pieceColor, ChessBoard board) {
        this.pieceColor = pieceColor;
        this.board = board;
    }

    public Collection<ChessMove> pieceMove(ChessPosition position) {
        List<ChessMove> legalMoves = new ArrayList<>();

        BishopMovesCalculator bishopMovesCalculator = new BishopMovesCalculator(pieceColor, board);
        legalMoves.addAll(bishopMovesCalculator.pieceMove(position));

        RookMovesCalculator rookMovesCalculator = new RookMovesCalculator(pieceColor, board);
        legalMoves.addAll(rookMovesCalculator.pieceMove(position));

        return legalMoves;
    }
}
