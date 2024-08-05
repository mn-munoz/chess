package ui;

import com.google.gson.Gson;
import exception.ServerException;
import requestsresults.LoginRequest;
import requestsresults.LoginResult;
import requestsresults.RegisterRequest;
import requestsresults.RegisterResult;

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

        return this.makeRequest("POST", "/user", registerRequest, RegisterResult.class);
    }

    public LoginResult login(String username, String password) throws ServerException {
        LoginRequest loginRequest = new LoginRequest(username, password);

        return this.makeRequest("POST", "/session", loginRequest, LoginResult.class);
    }

    public void clear() {

    }

    public void logout() {

    }

    public void createGame() {

    }

    public void listGames() {

    }

    public void joinGame() {

    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ServerException {
        try {
            URL url = (new URI(baseUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeRequest(request, http);
            http.connect();
            if (http.getResponseCode() >= 400) {
                throw new ServerException("Error trying to make request: " + http.getResponseCode());
            }
            return readResponse(http, responseClass);
        } catch (Exception ex) {
            throw new ServerException(ex.getMessage());
        }
    }

    private static void writeRequest(Object request, HttpURLConnection http) throws IOException {
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
