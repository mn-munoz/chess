package ui;

import exception.ServerException;
import ui.communicators.HttpCommunicator;
import ui.facaderequests.FacadeCreateGame;
import ui.facaderequests.FacadeJoinGame;
import ui.facaderequests.FacadeLogin;
import ui.facaderequests.FacadeRegister;
import ui.facaderesults.FacadeCreateGameResult;
import ui.facaderesults.FacadeListGamesResult;
import ui.facaderesults.FacadeLoginResult;
import ui.facaderesults.FacadeRegisterResult;

public class ServerFacade {

    private final HttpCommunicator httpCommunicator;

    public ServerFacade(int port) {
        this.httpCommunicator = new HttpCommunicator("http://localhost:" + port);
    }

    public FacadeRegisterResult register(String user, String password, String email) throws ServerException {
        FacadeRegister registerRequest = new FacadeRegister(user, password, email);
        try {
            return httpCommunicator.makeRequest("POST", "/user", registerRequest, FacadeRegisterResult.class, null);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    public FacadeLoginResult login(String username, String password) throws ServerException {
        FacadeLogin loginRequest = new FacadeLogin(username, password);
        try {
            return httpCommunicator.makeRequest("POST", "/session", loginRequest, FacadeLoginResult.class, null);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    public void clear() throws ServerException {
        try {
            httpCommunicator.makeRequest("DELETE", "/db", null, null, null);
        } catch (Exception ex) {
            throw new ServerException(ex.getMessage());
        }
    }

    public void logout(String authToken) throws ServerException {
        try {
            httpCommunicator.makeRequest("DELETE", "/session", null, null, authToken);
        } catch (Exception ex) {
            throw new ServerException(ex.getMessage());
        }
    }

    public void createGame(String authToken, String gameName) throws ServerException {
        FacadeCreateGame createGameRequest = new FacadeCreateGame(gameName);
        try {
            httpCommunicator.makeRequest("POST", "/game", createGameRequest, FacadeCreateGameResult.class, authToken);
        } catch (Exception ex) {
            throw new ServerException(ex.getMessage());
        }
    }

    public FacadeListGamesResult listGames(String authToken) throws ServerException {
        try {
            return httpCommunicator.makeRequest("GET", "/game", null, FacadeListGamesResult.class, authToken);
        } catch (Exception ex) {
            throw new ServerException(ex.getMessage());
        }
    }

    public void joinGame(String authToken, int gameId, String teamColor) throws ServerException {
        FacadeJoinGame joinGameRequest = new FacadeJoinGame(teamColor, gameId);
        try {
            httpCommunicator.makeRequest("PUT", "/game", joinGameRequest, null, authToken);
        } catch (Exception ex) {
            throw new ServerException(ex.getMessage());
        }
    }

}
