package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ServerException;
import websocket.commands.LeaveCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;
import java.lang.Math;

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
                            System.out.println(e.getMessage());
                        }
                    }
                    break;
                case "redraw":
                    printChessboard(game, teamColor);
                    break;
                default:
                    System.out.println("Invalid command");
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
        System.out.println("move - <EXAMPLE: c2c3> moves chess piece");
        System.out.println("resign - forfeit game (Will not leave game)");
        System.out.println("highlight - <EXAMPLE: c2> highlights all legal moves for given piece");
    }

    private void printChessboard(ChessGame game, String teamColor) {
        int modifier = 0;
        if (teamColor.equalsIgnoreCase("BLACK")) {
            modifier = 9;
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {

                if (i == 0 || i == 9) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    System.out.print(" " + getLetter(Math.abs(modifier - j)) + " ");
                    System.out.print(EscapeSequences.RESET_BG_COLOR);
                }

                if (j == 0 || j == 9) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    if (i > 0 && i < 9) {
                        System.out.print(" " + (9 - i) + " ");
                    }
                    System.out.print(EscapeSequences.RESET_BG_COLOR);
                }

                if (i > 0 && i < 9 && j > 0 && j < 9) {
                    if ((i + j) % 2 == 0) {
                        setWhiteSquare();
                    }
                    else {
                        setBlackSquare();
                    }
                    System.out.print(getPieceAtPosition(game.getBoard(),9 - i, Math.abs(modifier - j)));
                }
            }
            System.out.println();
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

    private void setBlackSquare() {
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
    }

    private void setWhiteSquare() {
        System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
    }

}
