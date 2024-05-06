package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMovesCalculator {
    private final ChessGame.TeamColor pieceColor;
    private final ChessBoard board;

    public PawnMovesCalculator(ChessGame.TeamColor pieceColor, ChessBoard board) {
        this.pieceColor = pieceColor;
        this.board = board;
    }
    public Collection<ChessMove> pieceMove(ChessPosition position) {
        List<ChessMove> legalMoves = new ArrayList<>();

        int currentRow = position.getRow();
        int currentCol = position.getColumn();

        if(currentRow == 2 || currentRow == 6){
            ChessPosition newPosition = new ChessPosition(currentRow + 2, currentCol);
            legalMoves.add(new ChessMove(position, newPosition, null));
        }
        else{
            ChessPosition forwardPosition = new ChessPosition(currentRow + 1, currentCol);
            ChessPiece newPositionPiece = board.getPiece(newPosition);
            if (forwardPositionPiece == null) {
                legalMoves.add(new ChessMove(position, newPosition, null));
            } else {
                if (newPositionPiece.getTeamColor() != pieceColor) {
                    legalMoves.add(new ChessMove(position, newPosition, null));
                }
            ChessPosition rightPosition = new ChessPosition(currentRow, currentCol + 1);
            legalMoves.add(new ChessMove(position, rightPosition, null));
            ChessPosition leftPosition = new ChessPosition(currentRow, currentCol -1);
            legalMoves.add(new ChessMove(position, leftPosition, null));
        }

        return legalMoves;
    }
}
