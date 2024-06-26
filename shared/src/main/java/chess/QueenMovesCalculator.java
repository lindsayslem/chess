package chess;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

public class QueenMovesCalculator {
    private final ChessGame.TeamColor pieceColor;
    private final ChessBoard board;

    public QueenMovesCalculator(ChessGame.TeamColor pieceColor, ChessBoard board) {
        this.pieceColor = pieceColor;
        this.board = board;
    }

    public Collection<ChessMove> pieceMove(ChessPosition position) {
        List<ChessMove> legalMoves = new ArrayList<>();

        RookMovesCalculator rookMovesCalculator = new RookMovesCalculator(pieceColor, board);
        BishopMovesCalculator bishopMovesCalculator = new BishopMovesCalculator(pieceColor, board);

        legalMoves.addAll(rookMovesCalculator.pieceMove(position));

        legalMoves.addAll(bishopMovesCalculator.pieceMove(position));

        return legalMoves;
    }
}