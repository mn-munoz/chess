package handlers;

import RequestsResults.JoinGameRequest;
import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import spark.Request;

public class JoinGameHandler extends Handler {
    private final JoinGameRequest joinRequest;

    private static class BodyContent {
        private String playerColor;
        private int gameID;

        public String getPlayerColor() {
            return playerColor;
        }

        public int getGameID() {
            return gameID;
        }
    }

    public JoinGameHandler(Request request) {
        String authToken = request.headers("authorization");
        BodyContent bodyContent = gson.fromJson(request.body(), BodyContent.class);

        this.joinRequest = new JoinGameRequest(authToken, bodyContent.getPlayerColor(), bodyContent.getGameID());
    }

    public String joinGame() throws DataAccessException {
        try {
            gameService.joinGame(joinRequest);
            return gson.toJson(new JsonObject());
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


}
