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

        if(currentRow == 2 || currentRow == 7){
            ChessPosition newPosition = new ChessPosition(currentRow + 2, currentCol);
            legalMoves.add(new ChessMove(position, newPosition, null));
        }
        else{
            ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol);
            ChessPiece newPositionPiece = board.getPiece(newPosition);
            if (newPositionPiece == null) {
                legalMoves.add(new ChessMove(position, newPosition, null));
            } else {
                ChessPosition diagonalRight = new ChessPosition(currentRow + 1, currentCol + 1);
                ChessPosition diagonalLeft = new ChessPosition(currentRow + 1, currentCol - 1);
                ChessPiece diagonalRightPiece = board.getPiece(diagonalRight);
                ChessPiece diagonalLeftPiece = board.getPiece(diagonalLeft);
                if (diagonalRightPiece.getTeamColor() != pieceColor) {
                    legalMoves.add(new ChessMove(position, diagonalRight, null));
                }
                else if(diagonalLeftPiece.getTeamColor() != pieceColor) {
                    legalMoves.add(new ChessMove(position, diagonalLeft, null));
                }
                }
        }
        return legalMoves;
    }
}
