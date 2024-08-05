package ui;

import exception.ServerException;

import java.util.Scanner;

public class PostLogin {
    private final String authToken;
    private final ServerFacade serverFacade;

    public PostLogin(String authToken, ServerFacade serverFacade) {
        this.authToken = authToken;
        this.serverFacade = serverFacade;
        postLoggedInLoop();
    }

    private void postLoggedInLoop() {
        boolean continueLoop = true;
        Scanner scanner = new Scanner(System.in);

        while (continueLoop) {
            System.out.print("\n[LOGGED IN] >>> ");
            String input = scanner.next();

            if (input.equalsIgnoreCase("quit")) {
                continueLoop = false;
            }
            else if (input.equalsIgnoreCase("logout")) {
                try {
                    serverFacade.logout(authToken);
                    continueLoop = false;
                } catch (ServerException e) {
                    System.out.println("Could not logout: " + e.getMessage());
                }
            }
            else if (input.equalsIgnoreCase("help")) {
                System.out.print(EscapeSequences.ERASE_SCREEN);
                System.out.flush();
                printMenu();
            }
            else if (input.equalsIgnoreCase("create")) {
                printChessboard();
            }
            else if (input.equalsIgnoreCase("list")) {
                printMenu();
            }
            else if (input.equalsIgnoreCase("join")) {
                printMenu();
            }
            else if (input.equalsIgnoreCase("observe")) {
                printMenu();
            }
            else {
                System.out.println("Invalid input");
            }
        }

    }

    private void printMenu() {
        System.out.println("create <NAME> - a game");
        System.out.println("list - games");
        System.out.println("join <ID> [WHITE|BLACK]");
        System.out.println("observe <ID> - a game");
        System.out.println("logout - when you are done");
        System.out.println("quit - playing chess");
        System.out.println("help - with possible commands");
    }

    private void printChessboard() {

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
                        System.out.print(" " + i + " ");
                    }
                    System.out.print(EscapeSequences.RESET_BG_COLOR);
                }

                String piece = getPieceAtPosition(i, j);
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
        if (row == 1) return getWhitePiece(col);
        if (row == 2) return EscapeSequences.WHITE_PAWN;
        if (row == 7) return EscapeSequences.BLACK_PAWN;
        if (row == 8) return getBlackPiece(col);
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
