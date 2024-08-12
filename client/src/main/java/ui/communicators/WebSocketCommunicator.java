package ui.communicators;

import com.google.gson.Gson;
import exception.ServerException;
import ui.ServerMessageObserver;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketCommunicator extends Endpoint {
    Session session;

    public WebSocketCommunicator(String baseUrl, ServerMessageObserver observer) throws ServerException {
        try {
            baseUrl = baseUrl.replace("http", "ws");
            URI socketURI = new URI(baseUrl + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Gson gson = new Gson();
                    ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
                    switch (serverMessage.getServerMessageType()) {
                        case LOAD_GAME -> serverMessage = gson.fromJson(message, LoadGameMessage.class);
                        case NOTIFICATION -> serverMessage = gson.fromJson(message, Notification.class);
                        case ERROR -> serverMessage = gson.fromJson(message, ErrorMessage.class);
                    }

                    observer.notify(serverMessage);
                }
            });

        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new ServerException(e.getMessage());
        }

    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void sendCommand(UserGameCommand command) throws ServerException {
        try {
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText(new Gson().toJson(command));
            } else {
                throw new ServerException("WebSocket session is not open.");
            }
        } catch (IOException e) {
            throw new ServerException("Failed to send command: " + e.getMessage());
        }
    }

}
