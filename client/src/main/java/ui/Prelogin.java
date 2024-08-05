package ui;
import requestsresults.RegisterResult;

import java.util.Scanner;



public class Prelogin {
    private static final String WELCOME_PHRASE =
            "Welcome to 240 Chess! type \"help\" to get started";
    private final ServerFacade serverFacade;

    public Prelogin() {
        this.serverFacade = new ServerFacade(8000);
        preloginPhase();
    }

    private void printMenu() {
        System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
        System.out.println("login <USERNAME> <PASSWORD> - to play chess");
        System.out.println("quit - playing chess");
        System.out.println("help - with possible commands");
    }

    private void preloginPhase() {
        Scanner scanner = new Scanner(System.in);
        boolean continueLoop = true;
        System.out.println(WELCOME_PHRASE);

        while (continueLoop) {
            System.out.print("[LOGGED OUT] >>> ");
            String input = scanner.next();
            if (input.equalsIgnoreCase("help")) {
                printMenu();
            }
            else if(input.equalsIgnoreCase("quit")) {
                continueLoop = false;
            }
            else if(input.equalsIgnoreCase("register")) {
                try {
                    String user = scanner.next();
                    String password = scanner.next();
                    String email = scanner.next();

                    RegisterResult response = serverFacade.register(user, password, email);
                    System.out.println(response.authToken() + " " + response.username());

                } catch (Exception e) {
                    System.out.println("Error trying to register");
                }
            }
            else if(input.equalsIgnoreCase("login")) {
                String user = scanner.next();
                String password = scanner.next();
            }
            else {
                System.out.println("Invalid argument. Try again");
            }

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
        }
    }

}
