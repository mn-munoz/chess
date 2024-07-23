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
        currentTeam = team;
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
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            // Check the color
            // Check if the move is in the promotion line of pawn
            if (piece.getTeamColor() == TeamColor.WHITE && move.getEndPosition().getRow() == 8
            || piece.getTeamColor() == TeamColor.BLACK && move.getEndPosition().getRow() == 1) {
                // if it is, create a new piece with the given promotion piece
                board.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
            } else {
                board.addPiece(move.getEndPosition(), piece);
            }
        }
        else {
            board.addPiece(move.getEndPosition(), piece);
        }

        if (isInCheck(piece.getTeamColor())) {
            setBoard(copy);
            throw new InvalidMoveException("This move puts king in check");

        }

        if (getTeamTurn() == TeamColor.WHITE) {
            setTeamTurn(TeamColor.BLACK);
        } else {
            setTeamTurn(TeamColor.WHITE);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */

    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKingPosition(teamColor);

        if (kingPosition == null) {
            return false;
        }

        return isPositionUnderAttack(kingPosition, teamColor);
    }

    private ChessPosition findKingPosition(TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                if (currentPiece != null && currentPiece.type == ChessPiece.PieceType.KING && currentPiece.getTeamColor() == teamColor) {
                    return new ChessPosition(currentPosition.getRow(), currentPosition.getColumn());
                }
            }
        }
        return null;
    }

    private boolean isPositionUnderAttack(ChessPosition position, TeamColor teamColor) {
        boolean underAttack = false;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                underAttack = underAttackHelper(underAttack, currentPiece, currentPosition, position, teamColor);
            }
        }
        return underAttack;
    }

    private boolean underAttackHelper(boolean underAttack, ChessPiece currentPiece,
                                      ChessPosition currentPosition, ChessPosition position, TeamColor teamColor) {
        if (currentPiece != null && currentPiece.getTeamColor() != teamColor) {
            Collection<ChessMove> moves = currentPiece.pieceMoves(board, currentPosition);
            for (ChessMove move : moves) {
                if (move.getEndPosition().equals(position)) {
                    underAttack = true;
                    return underAttack;
                }
            }
        }
        return underAttack;
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

        boolean isCheckmate = true;

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                if (currentPiece != null && currentPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = validMoves(currentPosition);
                    isCheckmate = checkmateHelper(isCheckmate, moves, currentPiece, teamColor);
                }
            }
        }

        return isCheckmate;
    }

    private boolean checkmateHelper(boolean isCheckmate, Collection<ChessMove> moves,
                                    ChessPiece currentPiece, TeamColor teamColor ) {
        if (moves != null) {
            for (ChessMove move : moves) {
                // "make" move in the board
                ChessBoard copy = new ChessBoard(board);
                copy.addPiece(move.getStartPosition(), null);
                copy.addPiece(move.getEndPosition(), currentPiece);

                // check if king in check
                if (!isInCheck(teamColor)) {
                    // there is at least one move to get king out of check
                    isCheckmate = false;
                }

                // undo the move
                setBoard(copy);
            }
        }

        return isCheckmate;
    }
    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean isInStalemate = !isInCheck(teamColor);

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                isInStalemate = stalemateHelper(isInStalemate, currentPiece, currentPosition, teamColor);
            }
        }

        return isInStalemate;
    }

    private boolean stalemateHelper(boolean isInStalemate, ChessPiece currentPiece,
                                    ChessPosition currentPosition, TeamColor teamColor) {
        if (currentPiece != null && currentPiece.getTeamColor() == teamColor) {
            Collection<ChessMove> moves = validMoves(currentPosition);
            if (moves != null && !moves.isEmpty()) {
                isInStalemate = false;
            }
        }

        return isInStalemate;
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return isWhitesTurn == chessGame.isWhitesTurn && currentTeam == chessGame.currentTeam && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isWhitesTurn, currentTeam, board);
    }
}
