package chess;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if(type == PieceType.BISHOP){
            BishopMovesCalculator bishopMovesCalculator = new BishopMovesCalculator(pieceColor, board);
            return bishopMovesCalculator.pieceMove(myPosition);
        }
        if(type == PieceType.KING){
            KingMovesCalculator kingMovesCalculator = new KingMovesCalculator(pieceColor, board);
            return kingMovesCalculator.pieceMove(myPosition);
        }
        if(type == PieceType.KNIGHT){
            KnightMovesCalculator knightMovesCalculator = new KnightMovesCalculator(pieceColor, board);
            return knightMovesCalculator.pieceMove(myPosition);
        }
        if(type == PieceType.PAWN){
            PawnMovesCalculator pawnMovesCalculator = new PawnMovesCalculator(pieceColor, board);
            return pawnMovesCalculator.pieceMove(myPosition);
        }
        if(type == PieceType.QUEEN){
            QueenMovesCalculator queenMovesCalculator = new QueenMovesCalculator(pieceColor, board);
            return queenMovesCalculator.pieceMove(myPosition);
        }
        if(type == PieceType.ROOK){
            RookMovesCalculator rookMovesCalculator = new RookMovesCalculator(pieceColor, board);
            return rookMovesCalculator.pieceMove(myPosition);
        }
        return null;
    }
}
