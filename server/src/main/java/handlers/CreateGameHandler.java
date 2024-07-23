package handlers;

import requestsresults.CreateGameRequest;
import dataaccess.DataAccessException;
import spark.Request;

public class CreateGameHandler extends Handler{
    private final CreateGameRequest createRequest;

    private record CreateRequestBody(String gameName){}

    public CreateGameHandler(Request request) {
        String authToken = request.headers("authorization");
        CreateRequestBody body = gson.fromJson(request.body(), CreateRequestBody.class);
        String gameName = body.gameName();
        this.createRequest = new CreateGameRequest(authToken, gameName);
    }

    public String createGame() throws DataAccessException {
        try {
            return gson.toJson(GAME_SERVICE.createGame(createRequest));
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
