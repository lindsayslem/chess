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
            KingMovementRule kingMovementRule = new KingMovementRule(board, pieceColor);
            for(ChessMove move : allMoves){
                ChessBoard tempBoard = kingMovementRule.copyBoard(board, move);
                KingMovementRule tempRule = new KingMovementRule(tempBoard, piece.getTeamColor());
                if (!tempRule.isInCheck()) {
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
        if(pieceColor != piece.getTeamColor()){
            throw new InvalidMoveException("invalid move - wrong team");
        }
        if(piece == null){
            throw new InvalidMoveException("invalid move-no piece at start");
        }
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if(!validMoves.contains(move)){
            throw new InvalidMoveException("invalid move");
        }
        KingMovementRule kingMovementRule = new KingMovementRule(board, pieceColor);
        ChessBoard tempBoard = kingMovementRule.copyBoard(board, move);
        KingMovementRule kingMovementRule1 = new KingMovementRule(tempBoard, pieceColor);

        if(kingMovementRule1.isInCheck()){
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