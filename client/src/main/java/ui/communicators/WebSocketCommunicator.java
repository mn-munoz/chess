package ui.communicators;

import com.google.gson.Gson;
import exception.ServerException;
import ui.ServerMessageObserver;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketCommunicator extends Endpoint {
    Session session;
    private final ServerMessageObserver observer;

    public WebSocketCommunicator(String baseUrl, ServerMessageObserver observer) throws ServerException {
        this.observer = observer;
        try {
            baseUrl = baseUrl.replace("http", "ws");
            URI socketURI = new URI(baseUrl + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
//                    Notification notification = new Gson().fromJson(message, Notification.class);
//                    notificationHandler.notify(notification);
                    observer.notify(serverMessage);
                }
            });

        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new ServerException(e.getMessage());
        }

    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        System.out.println("WebSocket opened");
        this.session = session;
    }

    public void sendCommand(UserGameCommand command) throws ServerException {
        try {
            if (session != null && session.isOpen()) {
                System.out.println("Sending command: " + command.getCommandType());
                session.getBasicRemote().sendText(new Gson().toJson(command));
            } else {
                throw new ServerException("WebSocket session is not open.");
            }
        } catch (IOException e) {
            throw new ServerException("Failed to send command: " + e.getMessage());
        }
    }

}
