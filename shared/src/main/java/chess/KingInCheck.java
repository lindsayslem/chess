package chess;

import java.util.Collection;

public class KingInCheck {
    public static boolean isKingInCheck(ChessBoard board, ChessGame.TeamColor teamColor) {
        ChessPosition kingPosition = findKingPosition(board, teamColor);
        if(kingPosition == null){
            return false;
        }
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){
                ChessPiece piece = board.getPiece(new ChessPosition(row +1, col+1));
                if(piece != null && piece.getTeamColor() != teamColor){
                    Collection<ChessMove> validMoves = piece.pieceMoves(board, new ChessPosition(row+1, col+1));
                    for(ChessMove move : validMoves){
                        if(move.getEndPosition().equals(kingPosition)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static ChessPosition findKingPosition(ChessBoard board, ChessGame.TeamColor teamColor) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row + 1, col + 1));
                if (piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    return new ChessPosition(row + 1, col + 1);
                }
            }
        }
        return null;
    }
}
