package chess;

import java.util.ArrayList;

public class PawnMoveCalculator extends MoveCalculator{
    @Override
    protected void moveCheck(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        ChessGame.TeamColor pieceColor = piece.getTeamColor();

        if (pieceColor == ChessGame.TeamColor.WHITE) {
            pawnCheck(avMvs, board, myPosition, pieceColor, 1, 7, 2);
        }
        // Case Black Pawn first move
        else if (pieceColor == ChessGame.TeamColor.BLACK) {
            pawnCheck(avMvs, board, myPosition, pieceColor, 6, 0, 0);
        }

    }

    private void pawnCheck(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor teamColor, int homeRow, int finalRow, int rowIncrement) {
        int modifier;
        if (teamColor == ChessGame.TeamColor.WHITE) {
            modifier = 1;
        } else {
            modifier = -1;
        }
        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();

        if(currentRow == homeRow) {
            ChessPosition twoAhead = new ChessPosition(currentRow + rowIncrement + modifier, currentCol + 1);
            ChessPosition oneAhead = new ChessPosition(currentRow + rowIncrement, currentCol + 1);

            if (isValidPosition(twoAhead.getRow(), twoAhead.getColumn()) &&
                    isValidPosition(oneAhead.getRow(), oneAhead.getColumn()) &&
                    board.getPiece(oneAhead) == null &&
                    board.getPiece(twoAhead) == null) {
                avMvs.add(new ChessMove(myPosition, twoAhead, null));
            }
        }
        // Can move straight ahead

        ChessPosition nextPosition = new ChessPosition(myPosition.getRow() + rowIncrement, myPosition.getColumn() + 1);
        if (isValidPosition(nextPosition.getRow(), nextPosition.getColumn())) {
            ChessPiece piece = board.getPiece(nextPosition);
            if (piece == null) {

                if (nextPosition.getRow() == finalRow) {
                    addMove(avMvs, myPosition, nextPosition, ChessPiece.PieceType.QUEEN);
                    addMove(avMvs, myPosition, nextPosition, ChessPiece.PieceType.ROOK);
                    addMove(avMvs, myPosition, nextPosition, ChessPiece.PieceType.BISHOP);
                    addMove(avMvs, myPosition, nextPosition, ChessPiece.PieceType.KNIGHT);
                } else {
                    addMove(avMvs, myPosition, nextPosition);
                }

            }
        }

        // can capture diagonally
        ChessPosition rightPosition = new ChessPosition(myPosition.getRow() + rowIncrement, myPosition.getColumn() + 2);
        ChessPosition leftPosition = new ChessPosition(myPosition.getRow() + rowIncrement, myPosition.getColumn());

        if (isValidPosition(rightPosition.getRow(), rightPosition.getColumn())) {
            ChessPiece rightPiece = board.getPiece(rightPosition);

            if (rightPiece != null && board.getPiece(myPosition).pieceColor != rightPiece.getTeamColor()) {
                if (rightPosition.getRow() == finalRow) {
                    addMove(avMvs, myPosition, rightPosition, ChessPiece.PieceType.QUEEN);
                    addMove(avMvs, myPosition, rightPosition, ChessPiece.PieceType.ROOK);
                    addMove(avMvs, myPosition, rightPosition, ChessPiece.PieceType.BISHOP);
                    addMove(avMvs, myPosition, rightPosition, ChessPiece.PieceType.KNIGHT);
                } else {
                    addMove(avMvs, myPosition, rightPosition);
                }
            }

        }


        if (isValidPosition(leftPosition.getRow(), leftPosition.getColumn())) {
            ChessPiece leftPiece = board.getPiece(leftPosition);
            if (leftPiece != null && board.getPiece(myPosition).pieceColor != leftPiece.getTeamColor()) {
                if (leftPosition.getRow() == finalRow) {
                    addMove(avMvs, myPosition, leftPosition, ChessPiece.PieceType.QUEEN);
                    addMove(avMvs, myPosition, leftPosition, ChessPiece.PieceType.ROOK);
                    addMove(avMvs, myPosition, leftPosition, ChessPiece.PieceType.BISHOP);
                    addMove(avMvs, myPosition, leftPosition, ChessPiece.PieceType.KNIGHT);
                } else {
                    addMove(avMvs, myPosition, leftPosition);
                }
            }
        }

    }
}
