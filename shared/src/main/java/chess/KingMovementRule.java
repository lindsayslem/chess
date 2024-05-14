package chess;

public class KingMovementRule {
    private final ChessBoard board;
    private final ChessGame.TeamColor currentTeam;

    public KingMovementRule(ChessBoard board, ChessGame.TeamColor currentTeam){
        this.board = board;
        this.currentTeam = currentTeam;
    }
    public boolean leavesKingInCheck(ChessMove move){
        ChessBoard copyBoard = copyBoard(board, move);
        ChessPosition kingPosition = findKingPosition(currentTeam, copyBoard);
        if(kingPosition != null){
            return KingInCheck.isKingInCheck(copyBoard, currentTeam);

        }
        return false;
    }

    private ChessPosition findKingPosition(ChessGame.TeamColor teamColor, ChessBoard board){
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
