package ui;
import requestsresults.LoginResult;
import ui.facaderesults.FacadeLoginResult;
import ui.facaderesults.FacadeRegisterResult;

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

                    FacadeRegisterResult response = serverFacade.register(user, password, email);
                    new PostLogin(response.authToken(), serverFacade);

                } catch (Exception e) {
                    System.out.println("Error trying to register");
                }
            }
            else if(input.equalsIgnoreCase("login")) {
                try {
                    String user = scanner.next();
                    String password = scanner.next();

                    FacadeLoginResult response = serverFacade.login(user, password);
                    new PostLogin(response.authToken(), serverFacade);

                } catch (Exception e) {
                    System.out.println("Error: Username and/or password not correct");
                }
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
