package java.passoff.server;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import java.net.HttpURLConnection;
import java.net.URL;

import server.Server;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerTests {
    private static final Server testServer = new Server();
    @BeforeAll
    public static void startServer() {
         testServer.run(8000);
    }

    @AfterAll
    static void stopServer() {
        testServer.stop();
    }

}
