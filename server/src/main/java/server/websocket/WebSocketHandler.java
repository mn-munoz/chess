package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DatabaseAuthDAO;
import dataaccess.DatabaseGameDAO;
import org.eclipse.jetty.websocket.api.Session;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.Notification;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final Gson gson = new Gson();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> handleConnect(session, command);
            case MAKE_MOVE -> handleMakeMove();
            case LEAVE -> justSomething();
            case RESIGN -> justSomething();
        }
    }

    public void justSomething() {
        System.out.println("just Something");
    }

    public void handleConnect(Session session, UserGameCommand command) throws IOException {
        System.out.println("Connecting...");
        System.out.println("WebSocket connection established.");
        DatabaseGameDAO gameDAO;
        DatabaseAuthDAO authDAO;
        try {
            gameDAO = new DatabaseGameDAO();
            authDAO = new DatabaseAuthDAO();

            ChessGame game = gameDAO.getGame(command.getGameID()).game();
            String userName = authDAO.getAuth(command.getAuthToken()).username();

            LoadGameMessage loadGame = new LoadGameMessage(game);
            String jsonMessage = gson.toJson(loadGame);
            session.getRemote().sendString(jsonMessage);

            Notification notification = new Notification(userName + " has connected to the game.");
            connections.add(userName, session);
            connections.broadcast(userName, notification);
        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage("Error: " + e.getMessage());
            String errorJson = gson.toJson(errorMessage);

            if (session.isOpen()) {
                session.getRemote().sendString(errorJson);
            }
        }
    }

    public void handleMakeMove() {
        System.out.println("making move...");
    }
}
