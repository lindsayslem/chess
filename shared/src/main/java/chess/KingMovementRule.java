package chess;

import java.util.Collection;

public class KingMovementRule {
    private final ChessBoard board;
    private final ChessGame.TeamColor currentTeam;

    public KingMovementRule(ChessBoard board, ChessGame.TeamColor currentTeam){
        this.board = board;
        this.currentTeam = currentTeam;
    }
    public boolean isKingInCheck(ChessBoard board, ChessGame.TeamColor teamColor) {
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
    public boolean KingInCheckmate(ChessBoard board, ChessGame.TeamColor teamColor){
        if(!isKingInCheck(board, teamColor)){
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


    private ChessBoard copyBoard(ChessBoard board, ChessMove move){
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
