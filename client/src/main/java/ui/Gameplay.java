package ui;

import exception.ServerException;
import ui.communicators.WebSocketCommunicator;
import websocket.messages.ServerMessage;

public class Gameplay implements ServerMessageObserver{
    private final String authToken;
    private final ServerFacade serverFacade;
    private final int gameId;
    private final String teamColor;
    private WebSocketCommunicator ws;

    Gameplay(String authToken, int gameId, ServerFacade serverFacade, String teamColor) throws ServerException {
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

    public void showGame() {

    }

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case LOAD_GAME:
                if ("WHITE".equalsIgnoreCase(teamColor)) {
                    printChessboardWhite();
                } else if ("BLACK".equalsIgnoreCase(teamColor)) {
                    printChessboardBlack();
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

    private void printChessboardBlack() {

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {

                if (i == 0 || i == 9) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    System.out.print(" " + getLetter(9 - j) + " ");
                    System.out.print(EscapeSequences.RESET_BG_COLOR);
                }

                if (j == 0 || j == 9) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    if (i > 0 && i < 9) {
                        System.out.print(" " + i + " ");
                    }
                    System.out.print(EscapeSequences.RESET_BG_COLOR);
                }

                String piece = getPieceAtPosition(i, 9 - j);
                if (i > 0 && i < 9 && j > 0 && j < 9) {
                    if ((i + j) % 2 == 0) {
                        setWhiteSquare();
                    }
                    else {
                        setBlackSquare();
                    }
                    System.out.print(piece);
                }
            }
            System.out.println();
        }
    }

    private void printChessboardWhite() {

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {

                if (i == 0 || i == 9) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    System.out.print(" " + getLetter(j) + " ");
                    System.out.print(EscapeSequences.RESET_BG_COLOR);
                }

                if (j == 0 || j == 9) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    if (i > 0 && i < 9) {
                        System.out.print(" " + (9 - i) + " ");
                    }
                    System.out.print(EscapeSequences.RESET_BG_COLOR);
                }

                String piece = getPieceAtPosition( 9 - i, j);
                if (i > 0 && i < 9 && j > 0 && j < 9) {
                    if ((i + j) % 2 == 0) {
                        setWhiteSquare();
                    }
                    else {
                        setBlackSquare();
                    }
                    System.out.print(piece);
                }
            }
            System.out.println();
        }
    }

    private static String getPieceAtPosition(int row, int col) {
        if (row == 1) {
            return getWhitePiece(col);
        }
        if (row == 2) {
            return EscapeSequences.WHITE_PAWN;
        }
        if (row == 7) {
            return EscapeSequences.BLACK_PAWN;
        }
        if (row == 8) {
            return getBlackPiece(col);
        }
        return EscapeSequences.EMPTY;
    }

    private static String getWhitePiece(int col) {
        return switch (col) {
            case 1, 8 -> EscapeSequences.WHITE_ROOK;
            case 2, 7 -> EscapeSequences.WHITE_KNIGHT;
            case 3, 6 -> EscapeSequences.WHITE_BISHOP;
            case 4 -> EscapeSequences.WHITE_QUEEN;
            case 5 -> EscapeSequences.WHITE_KING;
            default -> EscapeSequences.EMPTY;
        };
    }

    private static String getBlackPiece(int col) {
        return switch (col) {
            case 1, 8 -> EscapeSequences.BLACK_ROOK;
            case 2, 7 -> EscapeSequences.BLACK_KNIGHT;
            case 3, 6 -> EscapeSequences.BLACK_BISHOP;
            case 4 -> EscapeSequences.BLACK_QUEEN;
            case 5 -> EscapeSequences.BLACK_KING;
            default -> EscapeSequences.EMPTY;
        };
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
