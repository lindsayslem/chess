package chess;

import java.util.Collection;

public class KingInCheckmate {
    public static boolean KingInCheckmate(ChessBoard board, ChessGame.TeamColor teamColor){
        if(!KingInCheck.isKingInCheck(board, teamColor)){
            return false;
        }
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){
                ChessPiece piece = board.getPiece(new ChessPosition(row +1, col+1));
                if(piece != null && piece.getTeamColor() == teamColor){
                    Collection<ChessMove> validMoves = piece.pieceMoves(board, new ChessPosition(row+1, col+1));
                    for(ChessMove move : validMoves){
                        ChessBoard copyBoard = copyBoard(board, move);
                        if(!KingInCheck.isKingInCheck(copyBoard, teamColor)){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private static ChessBoard copyBoard(ChessBoard board, ChessMove move){
        ChessBoard newBoard = new ChessBoard();
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++) {
                newBoard.addPiece(new ChessPosition(row +1, col+1), board.getPiece(new ChessPosition(row+1,col+1)));
            }
        }
        newBoard.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        newBoard.addPiece(move.getStartPosition(), null);
        return newBoard;
    }
}
