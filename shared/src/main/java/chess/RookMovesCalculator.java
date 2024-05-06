package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMovesCalculator {
    private final ChessGame.TeamColor pieceColor;
    private final ChessBoard board;

    public RookMovesCalculator(ChessGame.TeamColor pieceColor, ChessBoard board){
        this.pieceColor = pieceColor;
        this.board = board;
    }
    public Collection<ChessMove> pieceMove(ChessPosition position) {
        List<ChessMove> legalMoves = new ArrayList<>();

        int currentRow = position.getRow() + 1;

        while(currentRow > 0 && currentRow  < 8){
            ChessPosition newPosition = new ChessPosition(currentRow, position.getColumn());
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
                currentRow += 1;
            }
        return legalMoves;
        }

    }

