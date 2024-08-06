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
        FacadeRegisterResult result = facade.register("newUser", "1234", "e@e.com");
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.authToken().length() > 10);
    }

    @Test
    public void registerFailure() throws ServerException {
        facade.register("newUser", "12345", "a@e.com");
        Assertions.assertThrows(ServerException.class, () -> facade.register("newUser", "1234", "e@e.com"));
    }
}
