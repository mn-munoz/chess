package chess;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (position.getRow() >= 0 && position.getRow() <= 8 &&
                position.getColumn() >=0 && position.getColumn() <= 8) {
            squares[position.getRow() - 1][position.getColumn() - 1] = piece;
        }
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j] = null;
            }
        }

        resetPieces(ChessGame.TeamColor.WHITE, 1, 0);
        resetPieces(ChessGame.TeamColor.BLACK, 6, 7);
    }

    public void resetPieces(ChessGame.TeamColor color, int pawnRow, int finalRow){
        for (int i = 0; i < 8; i++) {
            squares[pawnRow][i] = new ChessPiece(color, ChessPiece.PieceType.PAWN);
        }

        squares[finalRow][0] = new ChessPiece(color, ChessPiece.PieceType.ROOK);
        squares[finalRow][1] = new ChessPiece(color, ChessPiece.PieceType.KNIGHT);
        squares[finalRow][2] = new ChessPiece(color, ChessPiece.PieceType.BISHOP);
        squares[finalRow][3] = new ChessPiece(color, ChessPiece.PieceType.QUEEN);
        squares[finalRow][4] = new ChessPiece(color, ChessPiece.PieceType.KING);
        squares[finalRow][5] = new ChessPiece(color, ChessPiece.PieceType.BISHOP);
        squares[finalRow][6] = new ChessPiece(color, ChessPiece.PieceType.KNIGHT);
        squares[finalRow][7] = new ChessPiece(color, ChessPiece.PieceType.ROOK);
    }

    private String pieceSymbols(ChessPiece.PieceType type) {
        return switch (type) {
            case KING -> "K";
            case QUEEN -> "Q";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case ROOK -> "R";
            case PAWN -> "P";
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = squares[i][j];
                if (piece != null) {
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        sb.append(pieceSymbols(piece.getPieceType()));
                    }
                    else {
                        sb.append(pieceSymbols(piece.getPieceType()).toLowerCase());
                    }
                }
                else {
                    sb.append(".");
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ChessBoard)) {
            return false;
        }
        return Arrays.deepEquals(squares, ((ChessBoard) obj).squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }
}
