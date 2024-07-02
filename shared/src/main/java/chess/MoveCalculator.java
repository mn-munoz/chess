package chess;

import java.util.ArrayList;

public abstract class MoveCalculator {
    protected  abstract void moveCheck(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition);
    
    public static MoveCalculator create(ChessPiece.PieceType type) {
        return switch (type) {
            case ROOK -> new RookMoveCalculator();
            case BISHOP -> new BishopMoveCalculator();
            case QUEEN -> new QueenMoveCalculator();
            case PAWN -> new PawnMoveCalculator();
            case KNIGHT -> new KnightMoveCalculator();
            case KING -> new KingMoveCalculator();
        };
    }

    protected void addMove(ArrayList<ChessMove> avMvs, ChessPosition myPosition, ChessPosition nextPosition, ChessPiece.PieceType promotion) {
        avMvs.add(new ChessMove(myPosition, nextPosition, promotion));
    }

    protected void addMove(ArrayList<ChessMove> avMvs, ChessPosition myPosition, ChessPosition nextPosition) {
        avMvs.add(new ChessMove(myPosition, nextPosition, null));
    }

    protected boolean isValidPosition(int row, int col) {
        return row >= 0 && row <= 7 && col >= 0 && col <= 7;
    }

    protected void addValidMove(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition, ChessPosition nextPosition) {
        ChessPiece fromPiece = board.getPiece(myPosition);
        ChessPiece toPiece = board.getPiece(nextPosition);

        if (toPiece == null || fromPiece.getTeamColor() != toPiece.getTeamColor()) {
            addMove(avMvs, myPosition, nextPosition);
        }
    }

    protected void checkNearby(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition, int rowIncrement, int colIncrement) {
        int row = myPosition.getRow() + rowIncrement;
        int col = myPosition.getColumn() + colIncrement;

        if (isValidPosition(row, col)) {
            ChessPosition nextPosition = new ChessPosition(row + 1, col + 1);
            ChessPiece piece = board.getPiece(nextPosition);

            if (piece == null || board.getPiece(myPosition).getTeamColor() != piece.getTeamColor()) {
                addMove(avMvs, myPosition, nextPosition);
            }
        }
    }

    protected void calculateMovesInDirection(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition, int rowIncrement, int colIncrement) {
        int row = myPosition.getRow() + rowIncrement;
        int col = myPosition.getColumn() + colIncrement;

        while (isValidPosition(row, col)) {
            ChessPosition nextPosition = new ChessPosition(row + 1, col + 1);
            addValidMove(avMvs, board, myPosition, nextPosition);

            if (board.getPiece(nextPosition) != null) {
                break;
            }

            row += rowIncrement;
            col += colIncrement;
        }
    }

    protected void diagonalCheck(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition) {
        calculateMovesInDirection(avMvs, board, myPosition, 1, 1);  // diagonal down-right
        calculateMovesInDirection(avMvs, board, myPosition, 1, -1); // diagonal down-left
        calculateMovesInDirection(avMvs, board, myPosition, -1, 1); // diagonal up-right
        calculateMovesInDirection(avMvs, board, myPosition, -1, -1); // diagonal up-left
    }

    protected void calculateHorizontalMoves(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition) {
        calculateMovesInDirection(avMvs, board, myPosition, 0, 1);  // right
        calculateMovesInDirection(avMvs, board, myPosition, 0, -1); // left
    }

    protected void calculateVerticalMoves(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition) {
        calculateMovesInDirection(avMvs, board, myPosition, 1, 0);  // down
        calculateMovesInDirection(avMvs, board, myPosition, -1, 0); // up
    }

}
