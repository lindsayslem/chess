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

        if(pieceColor == ChessGame.TeamColor.WHITE){
            ChessPosition newPosition = new ChessPosition(currentRow + 1, currentCol);
            ChessPiece newPositionPiece = board.getPiece(newPosition);
            if(currentRow > 0 && currentRow < 9 && currentCol > 0 && currentCol < 9) {
                if (currentRow != 2 ) {
                    if (newPositionPiece == null && newPosition.getRow() != 8){
                        legalMoves.add(new ChessMove(position, newPosition, null));
                    }
                    if (board.getPiece(new ChessPosition(currentRow + 1, currentCol - 1)) != null) {
                        if(board.getPiece(new ChessPosition(currentRow + 1, currentCol - 1)).getTeamColor() != pieceColor) {
                            if (currentRow + 1 == 8){
                                legalMoves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentCol - 1), ChessPiece.PieceType.BISHOP));
                                legalMoves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentCol - 1), ChessPiece.PieceType.ROOK));
                                legalMoves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentCol - 1), ChessPiece.PieceType.KNIGHT));
                                legalMoves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentCol - 1), ChessPiece.PieceType.QUEEN));
                            }
                            else {
                                legalMoves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentCol - 1), null));
                            }
                        }
                    }if (board.getPiece(new ChessPosition(currentRow + 1, currentCol + 1)) != null) {
                        if (board.getPiece(new ChessPosition(currentRow + 1, currentCol + 1)).getTeamColor() != pieceColor) {
                            if (currentRow + 1 == 8){
                                legalMoves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentCol + 1), ChessPiece.PieceType.BISHOP));
                                legalMoves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentCol + 1), ChessPiece.PieceType.ROOK));
                                legalMoves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentCol + 1), ChessPiece.PieceType.KNIGHT));
                                legalMoves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentCol + 1), ChessPiece.PieceType.QUEEN));
                            }
                            else {
                                legalMoves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentCol + 1), null));
                            }                        }
                    }

                }
                if (currentRow == 2) {
                    ChessPosition startMove = new ChessPosition(currentRow + 2, currentCol);
                    startingMoves(position, legalMoves, newPosition, newPositionPiece, startMove);
                }
                if (newPosition.getRow() == 8) {
                    promotingPawns(position, legalMoves, newPosition);
                }

            }

        }
        else{
            ChessPosition newPosition = new ChessPosition(currentRow - 1, currentCol);
            ChessPiece newPositionPiece = board.getPiece(newPosition);
            if(currentRow > 0 && currentRow < 9 && currentCol > 0 && currentCol < 9) {
                if (currentRow != 7 ) {
                    if (newPositionPiece == null && newPosition.getRow() != 1) {
                        legalMoves.add(new ChessMove(position, newPosition, null));
                    }

                    if (board.getPiece(new ChessPosition(currentRow - 1, currentCol - 1)) != null) {
                        if (board.getPiece(new ChessPosition(currentRow - 1, currentCol - 1)).getTeamColor() != pieceColor) {
                            if (currentRow - 1 == 1) {
                                legalMoves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentCol - 1), ChessPiece.PieceType.BISHOP));
                                legalMoves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentCol - 1), ChessPiece.PieceType.ROOK));
                                legalMoves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentCol - 1), ChessPiece.PieceType.KNIGHT));
                                legalMoves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentCol - 1), ChessPiece.PieceType.QUEEN));
                            } else {
                                legalMoves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentCol - 1), null));
                            }
                        }
                    }
                    if (board.getPiece(new ChessPosition(currentRow - 1, currentCol + 1)) != null) {
                        if (board.getPiece(new ChessPosition(currentRow - 1, currentCol + 1)).getTeamColor() != pieceColor) {
                            if (currentRow - 1 == 1) {
                                legalMoves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentCol + 1), ChessPiece.PieceType.BISHOP));
                                legalMoves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentCol + 1), ChessPiece.PieceType.ROOK));
                                legalMoves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentCol + 1), ChessPiece.PieceType.KNIGHT));
                                legalMoves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentCol + 1), ChessPiece.PieceType.QUEEN));
                            } else {
                                legalMoves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentCol + 1), null));
                            }
                        }
                    }
                }

                if (currentRow == 7) {
                    ChessPosition startMove = new ChessPosition(currentRow - 2, currentCol);
                    startingMoves(position, legalMoves, newPosition, newPositionPiece, startMove);
                }
                if (newPosition.getRow() == 1) {
                    promotingPawns(position, legalMoves, newPosition);
                }


            }


        }
        return legalMoves;
    }

    private void startingMoves(ChessPosition position, List<ChessMove> legalMoves, ChessPosition newPosition, ChessPiece newPositionPiece, ChessPosition startMove) {
        ChessPiece startMovePositionPiece = board.getPiece(startMove);
        if (newPositionPiece == null) {
            legalMoves.add(new ChessMove(position, newPosition, null));
        }
        if(startMovePositionPiece == null && newPositionPiece == null){
            legalMoves.add(new ChessMove(position, startMove, null));
        }
    }

    private void promotingPawns(ChessPosition position, List<ChessMove> legalMoves, ChessPosition newPosition) {
        legalMoves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.BISHOP));
        legalMoves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.ROOK));
        legalMoves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.KNIGHT));
        legalMoves.add(new ChessMove(position, newPosition, ChessPiece.PieceType.QUEEN));
    }


}