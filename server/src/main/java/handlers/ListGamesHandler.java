package handlers;

import requestsresults.ListGamesRequest;
import dataaccess.DataAccessException;
import spark.*;

public class ListGamesHandler extends Handler{

    private final ListGamesRequest listGamesRequest;

    public ListGamesHandler(Request request) {
        String authToken = request.headers("authorization");

        listGamesRequest = new ListGamesRequest(authToken);
    }

    public String listGames() throws DataAccessException {
        try {
            return gson.toJson(GAME_SERVICE.listGames(listGamesRequest));
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
