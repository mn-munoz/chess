package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String visitorName;
    public Session session;
    public int gameId;

    public Connection(String visitorName, Session session, int gameId) {
        this.visitorName = visitorName;
        this.session = session;
        this.gameId = gameId;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}
