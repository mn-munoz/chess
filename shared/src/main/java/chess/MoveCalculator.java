package chess;

import java.util.ArrayList;

public abstract class MoveCalculator {
    public abstract void moveCheck(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition);

    public static MoveCalculator create(ChessPiece.PieceType type) {
        return switch (type) {
            case KING -> new KingMoveCalculator();
            case QUEEN -> new QueenMoveCalculator();
            case BISHOP -> new BishopMoveCalculator();
            case ROOK -> new RookMoveCalculator();
            case KNIGHT -> new KnightMoveCalculator();
            case PAWN -> new PawnMoveCalculator();
        };
    }

    protected void addMove(ArrayList<ChessMove> avMvs, ChessPosition myPosition, ChessPosition nextPosition, ChessPiece.PieceType promotion) {
        avMvs.add(new ChessMove(myPosition,nextPosition, promotion));
    }

    protected void addMove(ArrayList<ChessMove> avMvs, ChessPosition myPosition, ChessPosition nextPosition) {
        avMvs.add(new ChessMove(myPosition,nextPosition, null));
    }

    protected boolean validMove(int nextRow, int nextColum) {
        return nextRow > 0 && nextRow <= 8 && nextColum > 0 && nextColum <=8;
    }

    protected void checkForDirection(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition, int rowIncrement, int colIncrement) {
        int nextRow = myPosition.getRow() + rowIncrement;
        int nextColum = myPosition.getColumn() + colIncrement;

        while (validMove(nextRow, nextColum)) {
            ChessPosition nextPosition = new ChessPosition(nextRow, nextColum);

            if (board.getPiece(nextPosition) == null) {
                addMove(avMvs, myPosition, nextPosition);
            }
            else {
                if(board.getPiece(nextPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    addMove(avMvs, myPosition, nextPosition);
                }
                break;
            }

            nextRow += rowIncrement;
            nextColum += colIncrement;
        }
    }

    protected void checkNearby(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition, int rowIncrement, int colIncrement) {
        int nextRow = myPosition.getRow() + rowIncrement;
        int nextColum = myPosition.getColumn() + colIncrement;

        if (validMove(nextRow, nextColum)) {
            ChessPosition nextPosition = new ChessPosition(nextRow, nextColum);

            if (board.getPiece(nextPosition) == null || board.getPiece(nextPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                addMove(avMvs, myPosition, nextPosition);
            }

        }

    }

    protected void diagonalCheck(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition) {
        checkForDirection(avMvs, board, myPosition, 1,1 );
        checkForDirection(avMvs, board, myPosition, -1,1 );
        checkForDirection(avMvs, board, myPosition, 1,-1 );
        checkForDirection(avMvs, board, myPosition, -1,-1 );
    }

    protected void straightCheck(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition) {
        checkForDirection(avMvs, board, myPosition, 0,-1 );
        checkForDirection(avMvs, board, myPosition, 0,1 );
        checkForDirection(avMvs, board, myPosition, 1,0 );
        checkForDirection(avMvs, board, myPosition, -1,0 );
    }
}
