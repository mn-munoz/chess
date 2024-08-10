package ui.communicators;

import com.google.gson.Gson;
import exception.ServerException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class HttpCommunicator {

    private final String baseUrl;

    public HttpCommunicator(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws IOException, URISyntaxException {
        URL url = (new URI(baseUrl + path)).toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod(method);
        http.setDoOutput(true);
        writeRequest(request, http, authToken);
        http.connect();
        if (http.getResponseCode() >= 400) {
            throw new IOException("Error trying to make request: " + http.getResponseCode());
        }
        return readResponse(http, responseClass);
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
