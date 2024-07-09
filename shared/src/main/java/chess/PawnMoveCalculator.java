package chess;

import java.util.ArrayList;

public class PawnMoveCalculator extends MoveCalculator{
    @Override
    public void moveCheck(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition) {
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            pawnCheck(avMvs, board, myPosition, 1, 7, 1);
        }
        else {
            pawnCheck(avMvs, board, myPosition, -1, 0, 6);
        }

    }

    private void pawnPromotion(ArrayList<ChessMove> avMvs, ChessPosition myPosition, ChessPosition nextPosition) {
        addMove(avMvs, myPosition, nextPosition, ChessPiece.PieceType.QUEEN);
        addMove(avMvs, myPosition, nextPosition, ChessPiece.PieceType.ROOK);
        addMove(avMvs, myPosition, nextPosition, ChessPiece.PieceType.BISHOP);
        addMove(avMvs, myPosition, nextPosition, ChessPiece.PieceType.KNIGHT);
    }

    private void pawnDiagonalCheck(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition, ChessPosition toPosition, int finalRow) {
        if (validMove(toPosition.getRow(), toPosition.getColumn())) {
            ChessPiece toPiece = board.getPiece(toPosition);
            if (toPiece != null && toPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                if (toPosition.getRow() == finalRow + 1) {
                    pawnPromotion(avMvs,myPosition, toPosition);
                }
                else {
                    addMove(avMvs, myPosition, toPosition);
                }
            }
        }
    }

    private void pawnCheck(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition, int rowIncrement, int finalRow, int homeRow) {
        int modifier;
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            modifier = 1;
        } else {
            modifier = -1;
        }

        // First Move
        if (myPosition.getRow() - 1 == homeRow) {
            ChessPosition twoAhead = new ChessPosition(myPosition.getRow() + rowIncrement + modifier, myPosition.getColumn());
            ChessPosition oneAhead = new ChessPosition(myPosition.getRow() + rowIncrement, myPosition.getColumn());

            if (validMove(oneAhead.getRow(), oneAhead.getColumn()) && board.getPiece(oneAhead) == null) {
                addMove(avMvs, myPosition, oneAhead);
            }
            if (validMove(twoAhead.getRow(), twoAhead.getColumn()) &&
                    validMove(oneAhead.getRow(), oneAhead.getColumn()) &&
                    board.getPiece(twoAhead) == null &&
                    board.getPiece(oneAhead) == null) {
                addMove(avMvs, myPosition, twoAhead);
            }
        }


        else {
            // Move Straight Ahead
            ChessPosition nextPosition = new ChessPosition(myPosition.getRow() + rowIncrement, myPosition.getColumn());
            if (validMove(nextPosition.getRow(), nextPosition.getColumn())
                    && board.getPiece(nextPosition) == null) {
                if (nextPosition.getRow() - 1 == finalRow) {
                    pawnPromotion(avMvs, myPosition, nextPosition);
                }
                else {
                    addMove(avMvs,myPosition, nextPosition);
                }
            }

            // Capture Diagonally
            ChessPosition rightPosition = new ChessPosition(myPosition.getRow() + rowIncrement, myPosition.getColumn() + 1);
            ChessPosition leftPosition = new ChessPosition(myPosition.getRow() + rowIncrement, myPosition.getColumn() - 1);

            pawnDiagonalCheck(avMvs, board, myPosition, rightPosition, finalRow);
            pawnDiagonalCheck(avMvs, board, myPosition, leftPosition, finalRow);
        }
    }

}
