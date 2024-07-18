package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessGame.TeamColor pieceColor;
    private ChessBoard board;

    public ChessGame() {
        this.pieceColor = TeamColor.WHITE;
        this.board = new ChessBoard();
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return pieceColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        pieceColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Set<ChessMove> validMoves = new HashSet<>();
        ChessPiece piece = board.getPiece(startPosition);
        if(piece != null){
            Collection<ChessMove> allMoves = piece.pieceMoves(board, startPosition);
            for(ChessMove move : allMoves){
                ChessBoard tempBoard = copyBoard(board, move);
                ChessGame tempRule = new ChessGame();
                tempRule.setBoard(tempBoard);
                if (!tempRule.isInCheck(piece.getTeamColor())) {
                    validMoves.add(move);
                }
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if(piece == null){
            throw new InvalidMoveException("start is null");
        }
        if(pieceColor != piece.getTeamColor()){
            throw new InvalidMoveException("invalid move - wrong team");
        }

        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if(!validMoves.contains(move)){
            throw new InvalidMoveException("invalid move");
        }

        ChessBoard tempBoard = copyBoard(board, move);

        TeamColor opposingTeamColor = (pieceColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;

        if(isInCheck(opposingTeamColor
        )){
            throw new InvalidMoveException("King in danger");
        }
        board = tempBoard;
        pieceColor = (pieceColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKingPosition(teamColor);
        if(kingPosition == null){
            return false;
        }
        return isKingInCheck(kingPosition, teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
            return false;
        }
        return !hasValidMoves(teamColor);
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            return false;
        }
        return !hasValidMoves(teamColor);
    }

    private boolean hasValidMoves(TeamColor teamColor) {
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){
                ChessPiece piece = board.getPiece(new ChessPosition(row +1, col+1));
                if(piece != null && piece.getTeamColor() == teamColor){
                    Collection<ChessMove> moves = piece.pieceMoves(board, new ChessPosition(row+1, col+1));
                    for(ChessMove move : moves){
                        ChessBoard tempBoard = copyBoard(board, move);
                        ChessGame tempGame = new ChessGame();
                        tempGame.setBoard(tempBoard);
                        if(!tempGame.isInCheck(teamColor)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private ChessPosition findKingPosition(ChessGame.TeamColor teamColor) {
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

    private boolean isKingInCheck(ChessPosition kingPosition, TeamColor teamColor) {
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


    public ChessBoard copyBoard(ChessBoard board, ChessMove move){
        ChessBoard newBoard = new ChessBoard();
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row +1, col+1));
                if(piece != null) {
                    newBoard.addPiece(new ChessPosition(row + 1, col + 1), new ChessPiece(piece.getTeamColor(), piece.getPieceType()));
                }
            }
        }
        ChessPiece movingPiece = board.getPiece(move.getStartPosition());
        if(movingPiece != null){
            if(move.getPromotionPiece() != null) {
                newBoard.addPiece(move.getEndPosition(), new ChessPiece(movingPiece.getTeamColor(), move.getPromotionPiece()));
            }
            else{
                newBoard.addPiece(move.getEndPosition(), movingPiece);
            }
            newBoard.addPiece(move.getStartPosition(), null);
        }

        return newBoard;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}