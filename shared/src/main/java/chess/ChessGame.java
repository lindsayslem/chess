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
    private TeamColor currentTeam;
    private ChessBoard board;

    public ChessGame() {
        currentTeam = null;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeam = team;
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
        if(piece != null && piece.getTeamColor() == currentTeam){
            validMoves.addAll(piece.pieceMoves(board, startPosition));
            KingMovementRule kingMovementRule = new KingMovementRule(board, currentTeam);
            for(ChessMove move : new HashSet<>(validMoves)){
                if (kingMovementRule.isInCheck()) {
                    validMoves.remove(move);
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
        if(currentTeam != board.getPiece(move.getStartPosition()).getTeamColor()){
            throw new InvalidMoveException("invalid move-wrong team");
        }
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if(piece == null){
            throw new InvalidMoveException("invalid move-no piece at start");
        }
        Collection<ChessMove> validMoves = piece.pieceMoves(board, move.getStartPosition());
        if(!validMoves.contains(move)){
            throw new InvalidMoveException("invalid move");
        }
        ChessPiece originalPiece = board.getPiece(move.getEndPosition());
        board.addPiece(move.getEndPosition(), piece);
        board.addPiece(move.getStartPosition(), null);

        if(isInCheck(currentTeam)){
            board.addPiece(move.getEndPosition(), piece);
            board.addPiece(move.getStartPosition(), originalPiece);
            throw new InvalidMoveException("King in danger");
        }
        currentTeam = (currentTeam == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        KingMovementRule kingMovementRule = new KingMovementRule(board, teamColor);
        return kingMovementRule.isInCheck();
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        KingMovementRule kingMovementRule = new KingMovementRule(board, teamColor);
        return kingMovementRule.isInCheckmate();
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        KingMovementRule kingMovementRule = new KingMovementRule(board, teamColor);
        return kingMovementRule.isInStalemate();
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
