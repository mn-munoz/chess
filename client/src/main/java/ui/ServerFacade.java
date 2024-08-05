package ui;

import com.google.gson.Gson;
import exception.ServerException;
import requestsresults.*;
import ui.facaderequests.FacadeCreateGame;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;

public class ServerFacade {

    private String baseUrl = "http://localhost:";

    public ServerFacade(int port) {
        this.baseUrl = baseUrl + port;
    }

    public RegisterResult register(String user, String password, String email) throws ServerException {
        RegisterRequest registerRequest = new RegisterRequest(user, password, email);

        return this.makeRequest("POST", "/user", registerRequest, RegisterResult.class, null);
    }

    public LoginResult login(String username, String password) throws ServerException {
        LoginRequest loginRequest = new LoginRequest(username, password);

        return this.makeRequest("POST", "/session", loginRequest, LoginResult.class, null);
    }

    public void clear() {

    }

    public void logout(String authToken) throws ServerException {

        this.makeRequest("DELETE", "/session", null, null, authToken);
    }

    public CreateGameResult createGame(String authToken, String gameName) throws ServerException {
        FacadeCreateGame createGameRequest = new FacadeCreateGame(gameName);

        return makeRequest("POST", "/game", createGameRequest, CreateGameResult.class, authToken);
    }

    public void listGames() {

    }

    public void joinGame() {

    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ServerException {
        try {
            URL url = (new URI(baseUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            writeRequest(request, http, authToken);
            http.connect();
            if (http.getResponseCode() >= 400) {
                throw new ServerException("Error trying to make request: " + http.getResponseCode());
            }
            return readResponse(http, responseClass);
        } catch (Exception ex) {
            throw new ServerException(ex.getMessage());
        }
    }

    private static void writeRequest(Object request, HttpURLConnection http, String authToken) throws IOException {
        if (authToken != null) {
            http.setRequestProperty("Authorization", authToken);
        }

        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String requestData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(requestData.getBytes());
            }
        }
    }

    private static <T> T readResponse(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }
}
