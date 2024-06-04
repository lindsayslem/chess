package chess;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

public class RookMovesCalculator {
    private final ChessGame.TeamColor pieceColor;
    private final ChessBoard board;

    public RookMovesCalculator(ChessGame.TeamColor pieceColor, ChessBoard board){
        this.pieceColor = pieceColor;
        this.board = board;
    }

    public Collection<ChessMove> pieceMove(ChessPosition position){
        List<ChessMove> legalMoves = new ArrayList<>();

        int[][] moves = {{1, 0}, {-1, 0}, {0, -1}, {0, 1}};

        for(int[] move : moves){
            int rowDirection = move[0];
            int colDirection = move[1];

            int currentRow = position.getRow() + rowDirection;
            int currentCol = position.getColumn() + colDirection;

            while(currentRow > 0 && currentRow  < 9 && currentCol > 0 && currentCol < 9){
                ChessPosition newPosition = new ChessPosition(currentRow, currentCol);
                ChessPiece newPositionPiece = board.getPiece(newPosition);

                if(newPositionPiece == null){
                    legalMoves.add(new ChessMove(position, newPosition, null));
                }
                else{
                    if(newPositionPiece.getTeamColor() != pieceColor){
                        legalMoves.add(new ChessMove(position, newPosition, null));
                    }
                    break;
                }
                currentRow += rowDirection;
                currentCol += colDirection;
            }
        }
        /*returns all valid move*/
        return legalMoves;

    }
}