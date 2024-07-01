package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    ChessGame.TeamColor pieceColor;
    ChessPiece.PieceType type;

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
        ArrayList<ChessMove> availableMoves = new ArrayList<>();
        ChessPiece myPiece = board.getPiece(myPosition);
        switch (myPiece.type) {
            case KING:
                checkAround(availableMoves, board, myPosition);
                break;
            case QUEEN:
                horizontalMovesCheck(availableMoves, board, myPosition);
                verticalMovesCheck(availableMoves, board, myPosition);
                diagonalCheck(availableMoves, board, myPosition);
                break;
            case KNIGHT:
                lCheck(availableMoves, board, myPosition);
                break;
            case PAWN:
                pawnMovesCheck(availableMoves, board, myPosition);
                break;
            case ROOK: horizontalMovesCheck(availableMoves, board, myPosition);
                verticalMovesCheck(availableMoves, board, myPosition);
                break;
            case BISHOP:
                diagonalCheck(availableMoves, board, myPosition);
                break;
            default:  return availableMoves;
        }
        return availableMoves;
    }

    private void pawnMovesCheck(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition) {
        // Case White pawn first move
        if (board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.WHITE) {
            pawnCheck(avMvs, board, myPosition, pieceColor, 1, 7, 2);

        }
        // Case Black Pawn first move
        else if (board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.BLACK) {
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

        if (currentRow < 8 && currentRow == homeRow) {
            ChessPosition twoAhead = new ChessPosition(currentRow + rowIncrement + 1 * modifier, currentCol + 1);
            ChessPosition oneAhead = new ChessPosition(currentRow + rowIncrement, currentCol + 1);
            if (board.getPiece(twoAhead) == null && board.getPiece(oneAhead) == null) {
                avMvs.add(new ChessMove(myPosition, twoAhead, null));
                avMvs.add(new ChessMove(myPosition, oneAhead, null));
//            }
            }
        }
        // Can move straight ahead

        ChessPosition nextPosition = new ChessPosition(myPosition.getRow() + rowIncrement, myPosition.getColumn() + 1);
        ChessPiece piece = board.getPiece(nextPosition);
        if (piece == null) {
            if (nextPosition.getRow() == finalRow) {
                avMvs.add(new ChessMove(myPosition, nextPosition, PieceType.QUEEN));
                avMvs.add(new ChessMove(myPosition, nextPosition, PieceType.ROOK));
                avMvs.add(new ChessMove(myPosition, nextPosition, PieceType.BISHOP));
                avMvs.add(new ChessMove(myPosition, nextPosition, PieceType.KNIGHT));
            } else {
                avMvs.add(new ChessMove(myPosition, nextPosition, null));
            }
        }

            // can capture diagonally
            ChessPosition rightPosition = new ChessPosition(myPosition.getRow() + rowIncrement, myPosition.getColumn() + 2);
            ChessPosition leftPosition = new ChessPosition(myPosition.getRow() + rowIncrement, myPosition.getColumn());
            ChessPiece rightPiece = board.getPiece(rightPosition);
            ChessPiece leftPiece = board.getPiece(leftPosition);

            if (rightPiece != null && pieceColor != rightPiece.getTeamColor()) {
                if (rightPosition.getRow() == finalRow) {
                    avMvs.add(new ChessMove(myPosition, rightPosition, PieceType.QUEEN));
                    avMvs.add(new ChessMove(myPosition, rightPosition, PieceType.ROOK));
                    avMvs.add(new ChessMove(myPosition, rightPosition, PieceType.BISHOP));
                    avMvs.add(new ChessMove(myPosition, rightPosition, PieceType.KNIGHT));
                } else {
                    avMvs.add(new ChessMove(myPosition, rightPosition, null));
                }
            }


        if (leftPiece != null && pieceColor != leftPiece.getTeamColor()) {
            if (leftPosition.getRow() == finalRow) {
                avMvs.add(new ChessMove(myPosition, leftPosition, PieceType.QUEEN));
                avMvs.add(new ChessMove(myPosition, leftPosition, PieceType.ROOK));
                avMvs.add(new ChessMove(myPosition, leftPosition, PieceType.BISHOP));
                avMvs.add(new ChessMove(myPosition, leftPosition, PieceType.KNIGHT));
            } else {
                avMvs.add(new ChessMove(myPosition, leftPosition, null));
            }
        }

    }


    private void horizontalMovesCheck(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition) {
        ChessPosition nextPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
        boolean lookingLeft = true;
        boolean lookingRight = true;

        while (lookingLeft) {
            if (nextPosition.getColumn() < 0) {
                lookingLeft = false;
            }

            else if (board.getPiece(nextPosition) == null) {
                avMvs.add(new ChessMove(myPosition, nextPosition, null));
            } else {
                ChessPiece currPiece = board.getPiece(nextPosition);

                if (pieceColor != currPiece.getTeamColor()) {
                    avMvs.add(new ChessMove(myPosition, nextPosition, null));
                    break;
                } else {
                    break;
                }

            }

            nextPosition = new ChessPosition(myPosition.getRow() + 1, nextPosition.getColumn());
        }

        nextPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2);
        while (lookingRight) {
            if (nextPosition.getColumn() >= 8) {
                lookingRight = false;
            }

            else if (board.getPiece(nextPosition) == null) {
                avMvs.add(new ChessMove(myPosition, nextPosition, null));
            } else {
                ChessPiece currPiece = board.getPiece(nextPosition);

                if (pieceColor != currPiece.getTeamColor()) {
                    avMvs.add(new ChessMove(myPosition, nextPosition, null));
                    break;
                } else {
                    break;
                }

            }

            nextPosition = new ChessPosition(myPosition.getRow() + 1, nextPosition.getColumn() + 2);
        }

    }

    private void verticalMovesCheck(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition) {
        ChessPosition nextPosition = new ChessPosition(myPosition.getRow() , myPosition.getColumn() + 1);
        boolean lookingUp = true;
        boolean lookingDown = true;

        while (lookingDown) {
            if (nextPosition.getRow() < 0) {
                lookingDown = false;
            }

            else if (board.getPiece(nextPosition) == null) {
                avMvs.add(new ChessMove(myPosition, nextPosition, null));
            } else {
                ChessPiece currPiece = board.getPiece(nextPosition);

                if (pieceColor != currPiece.getTeamColor()) {
                    avMvs.add(new ChessMove(myPosition, nextPosition, null));
                    break;
                } else {
                    break;
                }

            }
            nextPosition = new ChessPosition(nextPosition.getRow(), myPosition.getColumn() + 1);
        }

        nextPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1);
        while (lookingUp) {
            if (nextPosition.getRow() >= 8) {
                lookingUp = false;
            }

            else if (board.getPiece(nextPosition) == null) {
                avMvs.add(new ChessMove(myPosition, nextPosition, null));
            } else {
                ChessPiece currPiece = board.getPiece(nextPosition);

                if (pieceColor != currPiece.getTeamColor()) {
                    avMvs.add(new ChessMove(myPosition, nextPosition, null));
                    break;
                } else {
                    break;
                }

            }

            nextPosition = new ChessPosition(nextPosition.getRow() + 2, myPosition.getColumn() + 1);
        }


    }

    private void diagonalCheck(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition) {
        // Check diagonally down-left
        checkDiagonal(avMvs, board, myPosition, -1, -1);

        // Check diagonally down-right
        checkDiagonal(avMvs, board, myPosition, -1, 1);

        // Check diagonally up-left
        checkDiagonal(avMvs, board, myPosition, 1, -1);

        // Check diagonally up-right
        checkDiagonal(avMvs, board, myPosition, 1, 1);
    }

    private void lCheck(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition) {
        // Check diagonally down-left
        checkNearby(avMvs, board, myPosition, -2, -1);
        checkNearby(avMvs, board, myPosition, -1, -2);

        // Check diagonally down-right
        checkNearby(avMvs, board, myPosition, -2, 1);
        checkNearby(avMvs, board, myPosition, -1, 2);

        // Check diagonally up-left
        checkNearby(avMvs, board, myPosition, 2, -1);
        checkNearby(avMvs, board, myPosition, 1, -2);

        // Check diagonally up-right
        checkNearby(avMvs, board, myPosition, 1, 2);
        checkNearby(avMvs, board, myPosition, 2, 1);
    }

    private void checkAround(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition) {
        // Check Up
        checkNearby(avMvs, board, myPosition, 1, 0);

        // Check Down
        checkNearby(avMvs, board, myPosition, -1, 0);

        // Check Left
        checkNearby(avMvs, board, myPosition, 0, -1);

        // Check Right
        checkNearby(avMvs, board, myPosition, 0, 1);

        // Check diagonally down-left
        checkNearby(avMvs, board, myPosition, -1, -1);

        // Check diagonally down-right
        checkNearby(avMvs, board, myPosition, -1, 1);

        // Check diagonally up-left
        checkNearby(avMvs, board, myPosition, 1, -1);

        // Check diagonally up-right
        checkNearby(avMvs, board, myPosition, 1, 1);


    }

    private void checkNearby(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition, int rowIncrement, int colIncrement) {
        int row = myPosition.getRow() + rowIncrement;
        int col = myPosition.getColumn() + colIncrement;

        if (row >= 0 && row <= 7 && col >= 0 && col <= 7) {
            ChessPosition nextPosition = new ChessPosition(row + 1, col + 1);
            ChessPiece piece = board.getPiece(nextPosition);

            if (piece == null) {
                avMvs.add(new ChessMove(myPosition, nextPosition, null));
            } else if(pieceColor != piece.getTeamColor()){
                avMvs.add(new ChessMove(myPosition, nextPosition, null));
            }
        }

    }

    private void checkDiagonal(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition, int rowIncrement, int colIncrement) {
        int row = myPosition.getRow() + rowIncrement;
        int col = myPosition.getColumn() + colIncrement;

        while (row >= 0 && row <= 7 && col >= 0 && col <= 7) {
            ChessPosition nextPosition = new ChessPosition(row + 1, col + 1);
            ChessPiece piece = board.getPiece(nextPosition);

            if (piece == null) {
                avMvs.add(new ChessMove(myPosition, nextPosition, null));
            } else {
                if (pieceColor != piece.getTeamColor()) {
                    avMvs.add(new ChessMove(myPosition, nextPosition, null));
                }
                break;
            }

            row += rowIncrement;
            col += colIncrement;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ChessPiece)) {
            return false;
        }

        ChessPiece temp = (ChessPiece)obj;

        return temp.type == this.type && temp.pieceColor == this.pieceColor;
    }

    @Override
    public int hashCode() {
        return 31 * this.type.hashCode() + this.pieceColor.hashCode();
    }
}
