package handlers;

import requestsresults.JoinGameRequest;
import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import spark.Request;

public class JoinGameHandler extends Handler {
    private final JoinGameRequest joinRequest;

    private record BodyContent(String playerColor, int gameID) {
    }

    public JoinGameHandler(Request request) {
        String authToken = request.headers("authorization");
        BodyContent bodyContent = gson.fromJson(request.body(), BodyContent.class);

        this.joinRequest = new JoinGameRequest(authToken, bodyContent.playerColor, bodyContent.gameID());
    }

    public String joinGame() throws DataAccessException {
        try {
            GAME_SERVICE.joinGame(joinRequest);
            return gson.toJson(new JsonObject());
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


}
