package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    boolean isWhitesTurn = true;
    TeamColor currentTeam;
    ChessBoard board;

    public ChessGame() {
        this.currentTeam = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     *
     */
    public TeamColor getTeamTurn() {
        return currentTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        if (team == TeamColor.WHITE) {
            currentTeam = TeamColor.BLACK;
        }
        else {
            currentTeam = TeamColor.WHITE;
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        Collection<ChessMove> availableMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();

        for (ChessMove move : availableMoves) {
            // "make" the move in the board
            ChessBoard copy = new ChessBoard(board);
            board.addPiece(move.getStartPosition(), null);
            board.addPiece(move.getEndPosition(), piece);

            // check if king in check
                if (!isInCheck(piece.pieceColor)) {
                    // if it isn't add to validMoves
                    validMoves.add(move);
                }
            // undo the move
            setBoard(copy);
            }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessBoard copy = new ChessBoard(board);
        ChessPiece piece = board.getPiece(move.getStartPosition());
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        boolean isValid = false;

        if (piece == null) {
            throw new InvalidMoveException("This is not a valid move");
        }
        if (piece.getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("It not this team's move");
        }
        if (validMoves == null || validMoves.isEmpty()) {
            throw new InvalidMoveException("No valid moves available");
        }

        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("This is not a valid move");
        }

        board.addPiece(move.getStartPosition(), null);
        board.addPiece(move.getStartPosition(), piece);

        if (isInCheck(piece.getTeamColor())) {
            throw new InvalidMoveException("This move puts king in check");
        }



    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = null;

        outerLoop:
        for (int i = 1; i <= 8;i++) {
            innerLoop:
            for (int j = 1; j <= 8;j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                if (board.getPiece(currentPosition) != null) {
                    ChessPiece currentPiece = board.getPiece(currentPosition);
                    if (currentPiece.type == ChessPiece.PieceType.KING && currentPiece.getTeamColor() == teamColor) {
                        kingPosition = new ChessPosition(currentPosition.getRow(), currentPosition.getColumn());
                        break outerLoop;
                    }
                }
            }
        }

        for (int i = 1; i <= 8;i++) {
            for (int j = 1; j <= 8;j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                if (currentPiece != null && currentPiece.getTeamColor() != teamColor) {
                    Collection<ChessMove> moves = currentPiece.pieceMoves(board, currentPosition);
                    for (ChessMove move : moves) {
                        if (currentPiece.getTeamColor() != teamColor && move.getEndPosition().equals(kingPosition) ) {
                            return true;
                        }
                    }

                }
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                if (currentPiece != null && currentPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = validMoves(currentPosition);
                    if (moves != null) {
                        for (ChessMove move : moves) {
                            // "make" move in the board
                            ChessBoard copy = new ChessBoard(board);
                            copy.addPiece(move.getStartPosition(), null);
                            copy.addPiece(move.getEndPosition(), currentPiece);

                            // check if king in check
                            if (!isInCheck(teamColor)) {
                                // there is at least one move to get king out of check
                                return false;
                            }

                            // undo the move
                            setBoard(copy);
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
       if(isInCheck(teamColor)) {
           return false;
       }

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                if (currentPiece != null && currentPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = validMoves(currentPosition);
                    if (moves != null && !moves.isEmpty()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return isWhitesTurn == chessGame.isWhitesTurn && currentTeam == chessGame.currentTeam && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isWhitesTurn, currentTeam, board);
    }
}
