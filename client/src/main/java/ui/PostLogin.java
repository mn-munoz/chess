package ui;

import exception.ServerException;
import model.GameSummary;
import ui.facaderesults.FacadeListGamesResult;

import java.util.HashMap;
import java.util.Scanner;


public class PostLogin {
    private final String authToken;
    private final ServerFacade serverFacade;
    private final HashMap<Integer, GameSummary> gameMap = new HashMap<>();
    private int activeGameID;

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
                System.exit(0);
            }
            else if (input.equalsIgnoreCase("logout")) {
                try {
                    serverFacade.logout(authToken);
                    continueLoop = false;
                } catch (ServerException e) {
                    System.out.println("Could not logout");
                }
            }
            else if (input.equalsIgnoreCase("help")) {
                printMenu();
            }
            else if (input.equalsIgnoreCase("create")) {
                String gameName = scanner.nextLine();
                try {
                    serverFacade.createGame(authToken, gameName);
                } catch (ServerException e) {
                    System.out.println("Unable to create game");
                }
            }
            else if (input.equalsIgnoreCase("list")) {
                try{
                    FacadeListGamesResult response = serverFacade.listGames(authToken);
                    int lastKeyGiven = 1;

                    for (GameSummary game: response.games()) {
                        gameMap.put(lastKeyGiven, game);
                        lastKeyGiven++;
                    }

                    for (int key: gameMap.keySet()) {
                        String whiteUser = gameMap.get(key).whiteUsername() != null ? gameMap.get(key).whiteUsername() : "[AVAILABLE]";
                        String blackUser = gameMap.get(key).blackUsername() != null ? gameMap.get(key).blackUsername() : "[AVAILABLE]";

                        System.out.println("Game Number: " + key + "->" +
                                "Game name: " + gameMap.get(key).gameName() +
                                " White user:" + whiteUser +
                                " Black user: " + blackUser);
                    }
                } catch (ServerException e) {
                    System.out.println("Unable to print out games available");
                }

            }
            else if (input.equalsIgnoreCase("join")) {
                try {
                    int gameId = scanner.nextInt();
                    String chessTeam = scanner.next().toUpperCase();

                    activeGameID = gameMap.get(gameId).gameID();

                    Gameplay gameplay = new Gameplay(authToken, activeGameID, serverFacade, chessTeam);
                    serverFacade.setObserver(gameplay);
                    gameplay.joinGame();
                    gameplay.gameLoop();

                } catch (Exception e) {
                    System.out.println("Unable to join game. Either game ID or team color not valid");
                }
            }
            else if (input.equalsIgnoreCase("observe")) {
                try {
                    if (gameMap.containsKey(scanner.nextInt())) {
                        Gameplay gameplay = new Gameplay(authToken, serverFacade, activeGameID);
                        serverFacade.setObserver(gameplay);
                    } else {
                        throw new IllegalArgumentException("not valid id");
                    }
                } catch (Exception e) {
                    System.out.println("ID not valid. Must be a number");
                }
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

}
