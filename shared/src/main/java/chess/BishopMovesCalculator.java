package chess;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

public class BishopMovesCalculator {
    private final ChessGame.TeamColor pieceColor;
    private final ChessBoard board;

    public BishopMovesCalculator(ChessGame.TeamColor pieceColor, ChessBoard board){
        this.pieceColor = pieceColor;
        this.board = board;
    }

    public Collection<ChessMove> pieceMove(ChessPosition position){
        List<ChessMove> legalMoves = new ArrayList<>();

        int[][] diagonals = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}};

        for(int[] diagonal : diagonals){
            int rowDirection = diagonal[0];
            int colDirection = diagonal[1];

            int currentRow = position.getRow() + rowDirection;
            int currentCol = position.getColumn() + colDirection;

            while(currentRow > 0 && currentRow  < 9 && currentCol > 0 && currentCol < 9){
                System.out.println("Current position: (" + currentRow + ", " + currentCol + ")");
                ChessPosition newPosition = new ChessPosition(currentRow, currentCol);
                ChessPiece newPositionPiece = board.getPiece(newPosition);

                if(newPositionPiece == null){
                    legalMoves.add(new ChessMove(position, newPosition, null));
                }
                else{
                    if(newPositionPiece.getTeamColor() != pieceColor){
                        legalMoves.add(new ChessMove(position, newPosition, null));
                    }
                    /*if(newPositionPiece.getTeamColor() == pieceColor){
                        break;
                    }*/
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
