package handlers;

import RequestsResults.CreateGameRequest;
import dataaccess.DataAccessException;
import spark.Request;

public class CreateGameHandler extends Handler{
    CreateGameRequest createRequest;

    public CreateGameHandler(Request request) {
        String authToken = request.headers("authorization");
        String gameName = request.body();
        this.createRequest = new CreateGameRequest(authToken, gameName);
    }

    public String createGame() throws DataAccessException {
        try {
            return gson.toJson(gameService.createGame(createRequest));
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
