package chess;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import static chess.RookMovesCalculator.rookAndBishopMovements;

public class BishopMovesCalculator {
    private final ChessGame.TeamColor pieceColor;
    private final ChessBoard board;

    public BishopMovesCalculator(ChessGame.TeamColor pieceColor, ChessBoard board){
        this.pieceColor = pieceColor;
        this.board = board;
    }

    public Collection<ChessMove> pieceMove(ChessPosition position){
        List<ChessMove> legalMoves = new ArrayList<>();

        int[][] moves = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}};

        return rookAndBishopMovements(position, legalMoves, moves, board, pieceColor);

    }
}