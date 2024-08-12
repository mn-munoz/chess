package ui;

import chess.*;
import exception.ServerException;
import websocket.commands.LeaveCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.ResignCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;
import java.lang.Math;

import java.util.ArrayList;
import java.util.Scanner;

public class Gameplay implements ServerMessageObserver{
    private final String authToken;
    private final ServerFacade serverFacade;
    private final int gameId;
    private final String teamColor;
    private ChessGame game;

    Gameplay(String authToken, int gameId, ServerFacade serverFacade, String teamColor) {
        this.authToken = authToken;
        this.serverFacade = serverFacade;
        this.serverFacade.setObserver(this);
        this.gameId = gameId;
        this.teamColor = teamColor;

    }

    Gameplay(String authToken, ServerFacade serverFacade, int gameId) {
        this.authToken = authToken;
        this.serverFacade = serverFacade;
        this.serverFacade.setObserver(this);
        this.gameId = gameId;
        this.teamColor = "WHITE";
    }

    public void joinGame() throws ServerException {
        serverFacade.joinGame(authToken, gameId, teamColor);
    }

    public void gameLoop() {
        Scanner scanner = new Scanner(System.in);
        boolean continueGameLoop = true;
        boolean hasResign = false;

        while (continueGameLoop) {
            System.out.println("Type \"help\" to see available commands.");
            System.out.print("[GAMEPLAY] >>> ");
            String[] input = scanner.nextLine().split("\\s");

            switch (input[0].toLowerCase()) {
                case "help":
                    if (input.length != 1 ) {
                        System.out.println("Help only takes one argument.");
                    } else {
                        printGameplayMenu();
                    }
                    break;
                case "leave":
                    if (input.length != 1 ) {
                        System.out.println("Leave only takes one argument.");
                    } else {
                        LeaveCommand command = new LeaveCommand(authToken, gameId);
                        try {
                            serverFacade.leaveGame(command);
                            continueGameLoop = false; // Exit the loop when leaving the game
                        } catch (ServerException e) {
                            System.out.println("Error trying to leave game");
                        }
                    }
                    break;
                case "redraw":
                    if (input.length != 1 ) {
                        System.out.println("Help only takes one argument.");
                    } else {
                        printChessboard(game, teamColor);
                    }
                    break;
                case "resign":
                    resignFunctionality(input);
                    hasResign = true;
                    break;
                case "highlight":
                    highlightFunctionality(input, hasResign);
                    break;
                case "move":
                    movePieceFunctionality(input, hasResign);
                    break;
                default:
                    System.out.println("Invalid command");
            }

        }

    }

    private void movePieceFunctionality(String[] input, boolean hasResign) {
        if (hasResign) {
            return;
        }

        if (input.length != 2) {
            System.out.println("Move command takes 1 argument: move c2c3");
            return;
        }
        String move = input[1].toUpperCase();

        if (move.length() != 4) {
            System.out.println("Move is not in correct format: Example - c2c3");
            return;
        }

        int startCol = getColumNum(move.charAt(0));
        int endCol = getColumNum(move.charAt(2));
        int startRow;
        int endRow;

        try {
            startRow = Character.getNumericValue(move.charAt(1));
            endRow = Character.getNumericValue(move.charAt(3));
        } catch (Exception e) {
            System.out.println("Unknown row number");
            return;
        }

        if (startCol == 0 || endCol == 0 ||
                startRow < 1 || startCol > 8 ||
                endRow < 1 || endRow > 8) {
            System.out.println("Invalid Move");
            return;
        }

        ChessPosition startPosition = new ChessPosition(startRow, startCol);
        ChessPosition endPosition = new ChessPosition(endRow, endCol);
        ChessPiece piece = game.getBoard().getPiece(startPosition);

        if (piece == null) {
            System.out.println("Empty spot selected");
            return;
        }

        ChessMove finalMove = createChessMove(startPosition, endPosition, piece);
        MakeMoveCommand command = new MakeMoveCommand(authToken, gameId, finalMove);

        try {
            serverFacade.makeMove(command);
        } catch (ServerException e) {
            System.out.println("Unable to make move due to: " + e.getMessage());
        }
    }

    private ChessMove createChessMove(ChessPosition startPosition, ChessPosition endPosition,
                                      ChessPiece piece) {

        if (piece.getPieceType() != ChessPiece.PieceType.PAWN) {
            return new ChessMove(startPosition, endPosition, null);
        }

        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE && endPosition.getRow() == 8
        || piece.getTeamColor() == ChessGame.TeamColor.BLACK && endPosition.getRow() == 1) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Pawn can be promoted to the following QUEEN, BISHOP, ROOK, KNIGHT: ");
            String promotion = scanner.nextLine();

            while (true) {
                switch (promotion.toLowerCase()) {
                    case "queen": return new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN);
                    case "bishop": return new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP);
                    case "rook": return new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK);
                    case "knight": return new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT);
                    default: System.out.println("Not valid promotion type");
                }
            }
        }

        return new ChessMove(startPosition, endPosition, null);
    }

    private int getColumNum(char letter) {
        return switch (letter) {
            case 'A' -> 1;
            case 'B' -> 2;
            case 'C' -> 3;
            case 'D' -> 4;
            case 'E' -> 5;
            case 'F' -> 6;
            case 'G' -> 7;
            case 'H' -> 8;
            default -> 0;
        };
    }

    private void highlightFunctionality(String[] input, boolean hasResign) {
        if (hasResign) {
            System.out.println("Cannot highlight pieces if resign");
            return;
        }
        if (input.length != 2) {
            System.out.println("Highlight command takes 1 argument: highlight a1");
            return;
        }

        String move = input[1].toUpperCase();
        if (move.length() != 2) {
            System.out.println("Move is not in correct format: Example - a1");
            return;
        }

        int colNum = getColumNum(move.charAt(0));
        int rowNum;
        try {
            rowNum = Character.getNumericValue(move.charAt(1));
        } catch (Exception e) {
            System.out.println("Unknown row number");
            return;
        }

        if (colNum == 0 || rowNum < 1 || rowNum > 8) {
            System.out.println("Invalid Move");
            return;
        }

        ChessPosition startPosition = new ChessPosition(rowNum, colNum);
        highlightPieceMoves(game, teamColor, startPosition);
    }

    private void resignFunctionality(String[] input) {
        if (input.length != 1 ) {
            System.out.println("Help only takes one argument.");
        } else {
            ResignCommand command = new ResignCommand(authToken, gameId);
            try {
                serverFacade.resignGame(command);
            } catch (ServerException e) {
                System.out.println("Error resigning from game");
                System.out.print(e.getMessage());
            }
        }

    }

    private void setGame(ChessGame game) {
        this.game = game;
    }

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case LOAD_GAME:
                LoadGameMessage loadGame = (LoadGameMessage) message;
                setGame(loadGame.getGame());
                System.out.println("\n");
                if ("WHITE".equalsIgnoreCase(teamColor)) {
                    printChessboard(game, teamColor);
                } else if ("BLACK".equalsIgnoreCase(teamColor)) {
                    printChessboard(game, teamColor);
                }
                break;
            case ERROR:
                System.out.println("Server error: " + message);
                break;
            case NOTIFICATION:
                System.out.println("Notification: " + message);
                break;
        }
    }

    private void printGameplayMenu() {
        System.out.println("redraw - redraws chessboard"); // done
        System.out.println("leave - leaves game and go back to menu"); // done
        System.out.println("move <EXAMPLE: c2c3> - moves chess piece");
        System.out.println("resign - forfeit game (Will not leave game)"); // I think done
        System.out.println("highlight - <EXAMPLE: c2> highlights all legal moves for given piece"); // done;
    }

    private void printChessboard(ChessGame game, String teamColor) {
        int modifier = 0;
        if (teamColor.equalsIgnoreCase("BLACK")) {
            modifier = 9;
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {

                setBorder(i, j, modifier);

                if (i > 0 && i < 9 && j > 0 && j < 9) {
                    if ((i + j) % 2 == 0) {
                        setSquare(EscapeSequences.SET_BG_COLOR_WHITE);
                    }
                    else {
                        setSquare(EscapeSequences.SET_BG_COLOR_DARK_GREY);
                    }
                    System.out.print(getPieceAtPosition(game.getBoard(),(Math.abs(9 - i - modifier)), Math.abs(modifier - j)));
                }
            }
            System.out.println();
        }

    }

    private void highlightPieceMoves(ChessGame game, String teamColor, ChessPosition position) {
        int modifier = 0;
        if (teamColor.equalsIgnoreCase("BLACK")) {
            modifier = 9;
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {

                setBorder(i, j, modifier);

                if (i > 0 && i < 9 && j > 0 && j < 9) {
                    if ((i + j) % 2 == 0) {
                        setSquare(EscapeSequences.SET_BG_COLOR_WHITE);
                    }
                    else {
                        setSquare(EscapeSequences.SET_BG_COLOR_DARK_GREY);
                    }
                    if (Math.abs(9 - i - modifier) == position.getRow() &&
                            Math.abs(modifier - j) == position.getColumn()) {
                        setSquare(EscapeSequences.SET_BG_COLOR_YELLOW);

                    }
                    highlightValidMoves(game, position, i, j, modifier);
                    System.out.print(getPieceAtPosition(game.getBoard(), Math.abs(9 - i - modifier), Math.abs(modifier - j)));
                }
            }
            System.out.println();
        }
    }

    private void highlightValidMoves(ChessGame game, ChessPosition position, int i, int j, int modifier) {
        ArrayList<ChessMove> validMoves = getValidMoves(game, position);
        for (ChessMove move : validMoves) {
            if (move.getEndPosition().getRow() == Math.abs(9 - i - modifier) &&
                    move.getEndPosition().getColumn() == Math.abs(modifier - j) &&
                    (i + j) % 2 == 0) {
                setSquare(EscapeSequences.SET_BG_COLOR_GREEN);
            }
            else if (move.getEndPosition().getRow() == Math.abs(9 - i - modifier) &&
                    move.getEndPosition().getColumn() == Math.abs(modifier - j) &&
                    !((i + j) % 2 == 0)) {
                setSquare(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
            }
        }
    }

    private ArrayList<ChessMove> getValidMoves(ChessGame game, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        ChessPiece piece = game.getBoard().getPiece(position);

        if (piece == null) {
            return validMoves;
        }

        validMoves = (ArrayList<ChessMove>) game.validMoves(position);

        return validMoves;
    }

    private void setBorder(int i, int j, int modifier) {
        if (i == 0 || i == 9) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(" " + getLetter(Math.abs(modifier - j)) + " ");
            System.out.print(EscapeSequences.RESET_BG_COLOR);
        }

        if (j == 0 || j == 9) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            if (i > 0 && i < 9) {
                System.out.print(" " + (Math.abs(9 - i - modifier)) + " ");
            }
            System.out.print(EscapeSequences.RESET_BG_COLOR);
        }
    }

    private String getPieceAtPosition(ChessBoard board, int row, int col) {
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));

        if (piece == null) {
            return EscapeSequences.EMPTY;
        }

        switch (piece.getPieceType()) {
            case KING:
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    return EscapeSequences.WHITE_KING;
                }
                else return EscapeSequences.BLACK_KING;
            case QUEEN:
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    return EscapeSequences.WHITE_QUEEN;
                }
                else return EscapeSequences.BLACK_QUEEN;
            case ROOK:
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    return EscapeSequences.WHITE_ROOK;
                }
                else return EscapeSequences.BLACK_ROOK;
            case BISHOP:
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    return EscapeSequences.WHITE_BISHOP;
                }
                else return EscapeSequences.BLACK_BISHOP;
            case KNIGHT:
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    return EscapeSequences.WHITE_KNIGHT;
                }
                else return EscapeSequences.BLACK_KNIGHT;
            case PAWN:
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    return EscapeSequences.WHITE_PAWN;
                }
                else return EscapeSequences.BLACK_PAWN;
        }

        return EscapeSequences.EMPTY;
    }

    private static char getLetter(int col) {
        return switch (col) {
            case 1 -> 'A';
            case 2 -> 'B';
            case 3 -> 'C';
            case 4 -> 'D';
            case 5 -> 'E';
            case 6 -> 'F';
            case 7 -> 'G';
            case 8 -> 'H';
            default -> ' ';
        };
    }

    private void setSquare(String sequence) {
        System.out.print(sequence);
    }

}
