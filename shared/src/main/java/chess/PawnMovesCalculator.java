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
                if (board.getPiece(newPosition) == null && board.getPiece(frontPosition) == null) {
                    legalMoves.add(new ChessMove(position, newPosition, null));
                    if (board.getPiece(frontPosition) == null) {
                        legalMoves.add(new ChessMove(position, frontPosition, null));

                    }                }
            }
            ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol);
            if (board.getPiece(newPosition) == null && newPosition.getRow() != 8 && newPosition.getRow() < 8) {
                legalMoves.add(new ChessMove(position, newPosition, null));
            }

            ChessPosition diagonalRight = new ChessPosition(currentRow + 1, currentCol+1);
            if (diagonalRight.getRow() <= 8 && diagonalRight.getRow() >= 1 && diagonalRight.getColumn() >= 1 && diagonalRight.getColumn() <= 8) {
                ChessPiece diagonalRightPiece = board.getPiece(diagonalRight);
                if (diagonalRightPiece != null && diagonalRightPiece.getTeamColor() != pieceColor) {
                    if(diagonalRight.getRow() != 8) {
                        legalMoves.add(new ChessMove(position, diagonalRight, null));
                    }
                    pawnPromotion(position, legalMoves, diagonalRight);
                }
            }
            ChessPosition diagonalLeft = new ChessPosition(currentRow + 1, currentCol-1);
            if (diagonalLeft.getRow() <= 8 && diagonalLeft.getRow() >= 1 && diagonalLeft.getColumn() >= 1 && diagonalLeft.getColumn() <= 8) {
                ChessPiece diagonalLeftPiece = board.getPiece(diagonalLeft);
                if (diagonalLeftPiece != null && diagonalLeftPiece.getTeamColor() != pieceColor) {
                    if(diagonalRight.getRow() != 8) {
                        legalMoves.add(new ChessMove(position, diagonalLeft, null));
                        System.out.println("(" + diagonalLeft.getRow() + ", " + diagonalLeft.getColumn() + ")");

                    }
                    if(diagonalLeft.getRow() == 8){
                        diagonalLeftPromotion(position, legalMoves, diagonalLeft);
                        System.out.println("(" + diagonalLeft.getRow() + ", " + diagonalLeft.getColumn() + ")");

                    }

                }
            }
            pawnPromotion(position, legalMoves, newPosition);

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
            if (board.getPiece(newPosition) == null && newPosition.getRow() != 1 && newPosition.getRow() < 8) {
                legalMoves.add(new ChessMove(position, newPosition, null));
            }
            ChessPosition diagonalRight = new ChessPosition(currentRow - 1, currentCol + 1);
            indexCheck(position, legalMoves, diagonalRight);
            ChessPosition diagonalLeft = new ChessPosition(currentRow - 1, currentCol - 1);
            indexCheck(position, legalMoves, diagonalLeft);
            if(newPosition.getRow() == 1){
                diagonalLeftPromotion(position, legalMoves, newPosition);
            }
        }

        return legalMoves;
    }

    private void indexCheck(ChessPosition position, List<ChessMove> legalMoves, ChessPosition diagonalRight) {
        if (diagonalRight.getRow() <= 8 && diagonalRight.getRow() >= 1 && diagonalRight.getColumn() >= 1 && diagonalRight.getColumn() <= 8) {
            ChessPiece diagonalRightPiece = board.getPiece(diagonalRight);
            if (diagonalRightPiece != null && diagonalRightPiece.getTeamColor() != pieceColor) {
                if(diagonalRight.getRow() != 1) {
                    legalMoves.add(new ChessMove(position, diagonalRight, null));
                }
                if(diagonalRight.getRow() == 1){
                    diagonalLeftPromotion(position, legalMoves, diagonalRight);
                }
            }
        }
    }

    private void diagonalLeftPromotion(ChessPosition position, List<ChessMove> legalMoves, ChessPosition diagonalLeft) {
        legalMoves.add(new ChessMove(position, diagonalLeft, ChessPiece.PieceType.BISHOP));
        legalMoves.add(new ChessMove(position, diagonalLeft, ChessPiece.PieceType.QUEEN));
        legalMoves.add(new ChessMove(position, diagonalLeft, ChessPiece.PieceType.KNIGHT));
        legalMoves.add(new ChessMove(position, diagonalLeft, ChessPiece.PieceType.ROOK));
    }

    private void pawnPromotion(ChessPosition position, List<ChessMove> legalMoves, ChessPosition diagonalRight) {
        if(diagonalRight.getRow() == 8){
            diagonalLeftPromotion(position, legalMoves, diagonalRight);
        }
    }
}