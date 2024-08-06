package client;

import exception.ServerException;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;
import ui.facaderesults.FacadeRegisterResult;


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
}
