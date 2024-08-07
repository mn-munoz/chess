package client;

import exception.ServerException;
import model.GameSummary;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;
import ui.facaderesults.FacadeRegisterResult;
import java.util.ArrayList;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade(port);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() throws ServerException {
        facade.clear();
        server.stop();
    }

    @Test
    public void clearTest() {
        Assertions.assertDoesNotThrow(facade::clear);

    }

    @Test
    public void registerSuccessTest() throws ServerException {
        FacadeRegisterResult result = facade.register("newUserTest", "1234", "e@e.com");
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.authToken().length() > 10);
    }

    @Test
    public void registerFailure() throws ServerException {
        facade.register("newUser", "12345", "a@e.com");
        Assertions.assertThrows(ServerException.class, () -> facade.register("newUser", "1234", "e@e.com"));
    }

    @Test
    public void loginSuccess() throws ServerException {
        facade.register("loginTest", "12345", "a@e.com");
        Assertions.assertDoesNotThrow(() -> facade.login("loginTest", "12345"));
    }

    @Test
    public void loginFailure() throws ServerException {
        facade.register("loginTestFailure", "12345", "a@e.com");
        Assertions.assertThrows(ServerException.class, () -> facade.login("loginTestFailure", "123"));
    }

    @Test
    public void logoutSuccess() throws ServerException {
        FacadeRegisterResult result = facade.register("LogoutTest", "12345", "a@e.com");
        Assertions.assertDoesNotThrow(() -> facade.logout(result.authToken()));
    }

    @Test
    public void logoutFailure() {
        Assertions.assertThrows(ServerException.class, () -> facade.logout(null));
    }

    @Test
    public void createGameSuccess() throws ServerException {
        FacadeRegisterResult result = facade.register("creteGameTest", "12345", "a@e.com");

        Assertions.assertDoesNotThrow(() -> facade.createGame(result.authToken(), "gameTest"));
    }

    @Test
    public void createGameFailure() {
        Assertions.assertThrows(ServerException.class, () -> facade.createGame(null, "gameTest"));
    }

    @Test
    public void listGamesSuccess() throws ServerException {
        FacadeRegisterResult result = facade.register("listGamesTest", "12345", "a@e.com");
        facade.createGame(result.authToken(), "testing");
        Assertions.assertDoesNotThrow(() -> facade.listGames(result.authToken()));
        Assertions.assertNotNull(facade.listGames(result.authToken()).games());
        Assertions.assertFalse(facade.listGames(result.authToken()).games().isEmpty());
    }

    @Test
    public void listGamesFailure() throws ServerException {
        FacadeRegisterResult result = facade.register("listGames", "12345", "a@e.com");
        facade.createGame(result.authToken(), "testing");
        Assertions.assertThrows(ServerException.class, () -> facade.listGames(null));
    }

    @Test
    public void joinGameSuccess() throws ServerException {
        FacadeRegisterResult result = facade.register("JoinGame", "12345", "a@e.com");
        facade.createGame(result.authToken(), "newGameToTest");
        ArrayList<GameSummary> gameList = (ArrayList<GameSummary>) facade.listGames(result.authToken()).games();
        int gameId = gameList.getFirst().gameID();
        Assertions.assertDoesNotThrow(() -> facade.joinGame(result.authToken(),gameId, "White".toUpperCase()));
    }

    @Test
    public void joinGameFailure() throws ServerException {
        FacadeRegisterResult result = facade.register("JoinGameFail", "12345", "a@e.com");
        facade.createGame(result.authToken(), "newGameToTest");
        ArrayList<GameSummary> gameList = (ArrayList<GameSummary>) facade.listGames(result.authToken()).games();
        int gameId = gameList.getFirst().gameID();
        Assertions.assertThrows(ServerException.class, () -> facade.joinGame(result.authToken(),gameId, "blue".toUpperCase()));
    }

    @Test
    public void joinGameNotAuthorized() throws ServerException {
        FacadeRegisterResult result = facade.register("JoinGameFailNotAuthorized", "12345", "a@e.com");
        facade.createGame(result.authToken(), "newGameToTest");
        ArrayList<GameSummary> gameList = (ArrayList<GameSummary>) facade.listGames(result.authToken()).games();
        int gameId = gameList.getFirst().gameID();
        Assertions.assertThrows(ServerException.class, () -> facade.joinGame(null,gameId, "blue".toUpperCase()));
    }

}
