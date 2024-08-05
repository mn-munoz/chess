package ui;

import exception.ServerException;

import java.util.Scanner;

public class PostLogin {
    private final String authToken;
    ServerFacade serverFacade;

    public PostLogin(String authToken, ServerFacade serverFacade) {
        this.authToken = authToken;
        this.serverFacade = serverFacade;
        postLoggedInLoop();
    }

    private void postLoggedInLoop() {
        boolean continueLoop = true;
        Scanner scanner = new Scanner(System.in);

        while (continueLoop) {
            printMenu();
            System.out.print("[LOGGED IN] >>> ");
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
}
