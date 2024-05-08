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

        if (pieceColor == ChessGame.TeamColor.WHITE) {
            if (currentRow == 2) {
                ChessPosition newPosition = new ChessPosition(position.getRow() + 2, currentCol);
                ChessPosition frontPosition = new ChessPosition(position.getRow() + 1, currentCol);
                if (board.getPiece(newPosition) == null) {
                    if (board.getPiece(frontPosition) == null) {
                        legalMoves.add(new ChessMove(position, newPosition, null));
                    }                }
            }
            ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol);
            if (board.getPiece(newPosition) == null && newPosition.getRow() != 8) {
                legalMoves.add(new ChessMove(position, newPosition, null));
            }
            ChessPosition diagonalRight = new ChessPosition(currentRow + 1, currentCol + 1);
            if (currentRow > 0 && currentRow < 9 && currentCol > 0 && currentCol < 9) {
                ChessPiece diagonalRightPiece = board.getPiece(diagonalRight);
                if (diagonalRightPiece != null && diagonalRightPiece.getTeamColor() != pieceColor) {
                    if(diagonalRight.getRow() != 8) {
                        legalMoves.add(new ChessMove(position, diagonalRight, null));
                    }
                    if(diagonalRight.getRow() == 8){
                        legalMoves.add(new ChessMove(position, diagonalRight, ChessPiece.PieceType.BISHOP));
                        legalMoves.add(new ChessMove(position, diagonalRight, ChessPiece.PieceType.QUEEN));
                        legalMoves.add(new ChessMove(position, diagonalRight, ChessPiece.PieceType.KNIGHT));
                        legalMoves.add(new ChessMove(position, diagonalRight, ChessPiece.PieceType.ROOK));
                    }
                }
            }
            ChessPosition diagonalLeft = new ChessPosition(currentRow + 1, currentCol - 1);
            if (currentRow > 0 && currentRow < 9 && currentCol > 0 && currentCol < 9) {
                ChessPiece diagonalLeftPiece = board.getPiece(diagonalLeft);
                if (diagonalLeftPiece != null && diagonalLeftPiece.getTeamColor() != pieceColor) {
                    if(diagonalRight.getRow() != 8) {
                        legalMoves.add(new ChessMove(position, diagonalLeft, null));
                    }
                    if(diagonalLeft.getRow() == 8){
                        legalMoves.add(new ChessMove(position, diagonalLeft, ChessPiece.PieceType.BISHOP));
                        legalMoves.add(new ChessMove(position, diagonalLeft, ChessPiece.PieceType.QUEEN));
                        legalMoves.add(new ChessMove(position, diagonalLeft, ChessPiece.PieceType.KNIGHT));
                        legalMoves.add(new ChessMove(position, diagonalLeft, ChessPiece.PieceType.ROOK));
                    }

                }
            }
            if(newPosition.getRow() == 8){
                legalMoves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.BISHOP));
                legalMoves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.QUEEN));
                legalMoves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.KNIGHT));
                legalMoves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.ROOK));

            }

        } else {
            if (currentRow == 7) {
                ChessPosition newPosition = new ChessPosition(position.getRow() - 2, currentCol);
                ChessPosition frontPosition = new ChessPosition(position.getRow() - 1, currentCol);
                if (board.getPiece(newPosition) == null) {
                    if (board.getPiece(frontPosition) == null) {
                        legalMoves.add(new ChessMove(position, newPosition, null));
                    }
                }
            }
            ChessPosition newPosition = new ChessPosition(currentRow - 1, currentCol);
            if (board.getPiece(newPosition) == null && newPosition.getRow() != 1) {
                legalMoves.add(new ChessMove(position, newPosition, null));

            }
            ChessPosition diagonalRight = new ChessPosition(currentRow - 1, currentCol + 1);
            if (currentRow > 0 && currentRow < 9 && currentCol > 0 && currentCol < 9) {
                ChessPiece diagonalRightPiece = board.getPiece(diagonalRight);
                if (diagonalRightPiece != null && diagonalRightPiece.getTeamColor() != pieceColor) {
                    if(diagonalRight.getRow() != 1) {
                        legalMoves.add(new ChessMove(position, diagonalRight, null));
                    }
                    if(diagonalRight.getRow() == 1){
                        legalMoves.add(new ChessMove(position, diagonalRight, ChessPiece.PieceType.BISHOP));
                        legalMoves.add(new ChessMove(position, diagonalRight, ChessPiece.PieceType.QUEEN));
                        legalMoves.add(new ChessMove(position, diagonalRight, ChessPiece.PieceType.KNIGHT));
                        legalMoves.add(new ChessMove(position, diagonalRight, ChessPiece.PieceType.ROOK));
                    }
                }
            }
            ChessPosition diagonalLeft = new ChessPosition(currentRow - 1, currentCol - 1);
            if (currentRow > 0 && currentRow < 9 && currentCol > 0 && currentCol < 9) {
                ChessPiece diagonalLeftPiece = board.getPiece(diagonalLeft);
                if (diagonalLeftPiece != null && diagonalLeftPiece.getTeamColor() != pieceColor) {
                    if(diagonalRight.getRow() != 1) {
                        legalMoves.add(new ChessMove(position, diagonalLeft, null));
                    }
                    if(diagonalLeft.getRow() == 1){
                        legalMoves.add(new ChessMove(position, diagonalLeft, ChessPiece.PieceType.BISHOP));
                        legalMoves.add(new ChessMove(position, diagonalLeft, ChessPiece.PieceType.QUEEN));
                        legalMoves.add(new ChessMove(position, diagonalLeft, ChessPiece.PieceType.KNIGHT));
                        legalMoves.add(new ChessMove(position, diagonalLeft, ChessPiece.PieceType.ROOK));
                    }
                }
            }
            if(newPosition.getRow() == 1){
                legalMoves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.BISHOP));
                legalMoves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.QUEEN));
                legalMoves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.KNIGHT));
                legalMoves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.ROOK));
            }
        }

        return legalMoves;
    }
}
