package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static chess.KingMovesCalculator.kingAndKnightMoves;

public class KnightMovesCalculator {
    private final ChessGame.TeamColor pieceColor;
    private final ChessBoard board;

    public KnightMovesCalculator(ChessGame.TeamColor pieceColor, ChessBoard board){
        this.pieceColor = pieceColor;
        this.board = board;
    }

    public Collection<ChessMove> pieceMove(ChessPosition position){
        List<ChessMove> legalMoves = new ArrayList<>();

        int[][] moves = {{2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}, {2, -1}};

        return kingAndKnightMoves(position, legalMoves, moves, board, pieceColor);
    }
}
