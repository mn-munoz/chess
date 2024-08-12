package server.websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseAuthDAO;
import dataaccess.DatabaseGameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.LeaveCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.Notification;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final Gson gson = new Gson();
    private DatabaseGameDAO gameDAO;
    private DatabaseAuthDAO authDAO;

    public WebSocketHandler() {
        setDataAccessObjects();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> handleConnect(session, command);
            case MAKE_MOVE -> handleMakeMove(session, gson.fromJson(message, MakeMoveCommand.class));
            case LEAVE -> handleLeave(session, new LeaveCommand(command.getAuthToken(), command.getGameID()));
            case RESIGN -> handleResign(session, command);
        }
    }

    public void handleLeave(Session session, UserGameCommand command) throws IOException {
        try {
            GameData gameData = gameDAO.getGame(command.getGameID());
            String userName = authDAO.getAuth(command.getAuthToken()).username();

            if (isPlayer(userName, gameData)) {
                setPlayerNull(gameData, userName);
            }

            Notification notification = new Notification(userName + " has left the game");
            connections.add(userName, session, gameData.gameID());
            connections.broadcast(userName, notification);

            connections.remove(userName);

        } catch (DataAccessException e) {
            errorManaging(session, e);
        }

    }

    public void handleResign(Session session, UserGameCommand command) throws IOException {
        try {
            GameData gameData = gameDAO.getGame(command.getGameID());
            ChessGame game = gameData.game();
            String userName = authDAO.getAuth(command.getAuthToken()).username();

            if (game.isGameOver()) {
                session.getRemote().sendString(gson.toJson(new ErrorMessage("The game is already over. No further actions can be taken.")));
                return;
            }

            if (!isPlayer(userName, gameData)) {
                throw new IllegalArgumentException("Observers cannot resign");
            }

            game.setGameOver(true);
            gameDAO.updateGame(gameData.gameID(), gameData);

            Notification notification = new Notification(userName + " has resigned.");
            String jsonMessage = gson.toJson(notification);
            session.getRemote().sendString(jsonMessage);
            connections.add(userName, session, gameData.gameID());
            connections.broadcast(userName, notification);


        } catch (Exception e) {
            errorManaging(session, e);
        }
    }

    public void handleConnect(Session session, UserGameCommand command) throws IOException {
        try {
            GameData gameData = gameDAO.getGame(command.getGameID());
            ChessGame game = gameData.game();
            String userName = authDAO.getAuth(command.getAuthToken()).username();
            String teamColor = setTeamColor(command, userName);

            LoadGameMessage loadGame = new LoadGameMessage(game);
            String jsonMessage = gson.toJson(loadGame);
            session.getRemote().sendString(jsonMessage);

            Notification notification = new Notification(userName + teamColor + " has connected to the game.");
            connections.add(userName, session, gameData.gameID());
            connections.broadcast(userName, notification);
        } catch (Exception e) {
            errorManaging(session, e);
        }
    }

    public void handleMakeMove(Session session, MakeMoveCommand command) throws IOException {
        try {
            GameData gameData = gameDAO.getGame(command.getGameID());
            ChessGame game = gameData.game();
            String userName = authDAO.getAuth(command.getAuthToken()).username();
            String teamColor = setTeamColor(command, userName);

            ChessMove move = command.getChessMove();

            if (!isPlayer(userName, gameData)) {
                throw new IllegalArgumentException(userName + " is an observer and is not allowed to make moves");
            }

            if (!isCorrectPlayer(gameData, userName)) {
                throw new IllegalArgumentException("Not allowed to move pieces from opposite team");
            }


            if (!game.isGameOver() && isValidMove(game, move)) {
                game.makeMove(move);

            } else {
                throw new IllegalArgumentException("Move " + move.getEndPosition() + " is not a valid move");
            }

            gameDAO.updateGame(command.getGameID(), gameData);


            LoadGameMessage loadGame = new LoadGameMessage(gameData.game());
            String jsonMessage = gson.toJson(loadGame);
            session.getRemote().sendString(jsonMessage);
            connections.broadcast(userName, loadGame);

            Notification notification = new Notification(userName + teamColor + " made a move!");
            connections.add(userName, session, gameData.gameID());
            connections.broadcast(userName, notification);

            if (isSpecialEvent(game, move)) {
                notification =  new Notification(userName + teamColor + " is in " + getSpecialEvent(game, move));
                connections.add(userName, session, gameData.gameID());
                connections.broadcast(userName, notification);
            }
        } catch (Exception e) {
            errorManaging(session, e);
        }
    }

    private void errorManaging(Session session, Exception e) throws IOException {
        ErrorMessage errorMessage = new ErrorMessage("Error: " + e.getMessage());
        String errorJson = gson.toJson(errorMessage);

        if (session.isOpen()) {
            session.getRemote().sendString(errorJson);
        }
    }

    private void setPlayerNull(GameData data, String username) throws DataAccessException {
        if (isPlayer(username, data)) {
            GameData newData = data;
            if (data.whiteUsername() != null && data.whiteUsername().equals(username)){
                newData = new GameData(data.gameID(), null, data.blackUsername(), data.gameName(), data.game());
            }
            if (data.blackUsername() != null && data.blackUsername().equals(username)){
                newData = new GameData(data.gameID(), data.whiteUsername(), null, data.gameName(), data.game());
            }
            try {
                gameDAO.updateGame(newData.gameID(), newData);
            }
            catch (DataAccessException e) {
                throw new DataAccessException("Unable to update game in database");
            }
        }
    }

    private boolean isPlayer(String username, GameData data) {
        if (data.whiteUsername() != null && data.whiteUsername().equals(username)) {
            return true;
        }
        return data.blackUsername() != null && data.blackUsername().equals(username);
    }

    private void setDataAccessObjects() {
        try {
            gameDAO = new DatabaseGameDAO();
            authDAO = new DatabaseAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String setTeamColor(UserGameCommand command, String userName) {
        try {
            if (gameDAO.getGame(command.getGameID()).whiteUsername().equalsIgnoreCase(userName)) {
                return "[WHITE]";
            }
            else {
                return "[BLACK]";
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValidMove(ChessGame game, ChessMove move) {
        for (ChessMove chessMove : game.validMoves(move.getStartPosition())) {
            if (chessMove.getEndPosition().equals(move.getEndPosition())) {
                return true;
            }
        }

        return false;
    }

    private boolean isCorrectPlayer(GameData data, String username) {
        if (data.game().getTeamTurn() == ChessGame.TeamColor.WHITE) {
            return data.whiteUsername().equals(username);
        }
        else {
            return data.blackUsername().equals(username);
        }
    }

    private ChessGame.TeamColor getTeamColor(ChessGame game, ChessPosition position) {
        ChessBoard board = game.getBoard();

        return board.getPiece(position).getTeamColor();
    }

    private boolean isSpecialEvent(ChessGame game, ChessMove move) {
        ChessGame.TeamColor teamColor = getTeamColor(game, move.getEndPosition());

        return game.isInCheck(teamColor) || game.isInCheckmate(teamColor) || game.isInStalemate(teamColor);
    }

    private String getSpecialEvent(ChessGame game, ChessMove move) {
        ChessGame.TeamColor teamColor = getTeamColor(game, move.getEndPosition());

        if (game.isInStalemate(teamColor)) {
            return "stalemate";
        }
        if (game.isInCheck(teamColor)) {
            return "check";
        }
        else {
            return "checkmate";
        }
    }
}
