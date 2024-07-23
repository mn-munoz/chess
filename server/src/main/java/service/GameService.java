package service;

import RequestsResults.CreateGameRequest;
import RequestsResults.CreateGameResult;
import RequestsResults.ListGamesRequest;
import RequestsResults.ListGamesResult;
import dataaccess.DataAccessException;
import model.GameData;

public class GameService extends Service{

    public void clear() {
        gameDAO.clear();
    }

    public ListGamesResult listGames(ListGamesRequest request) throws DataAccessException {
        try {
            validateToken(request.authToken());

            return new ListGamesResult(gameDAO.listGames());
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public CreateGameResult createGame(CreateGameRequest request) throws DataAccessException {
        try {
            validateToken(request.authToken());

            if (request.gameName() == null) {
                throw new DataAccessException("bad request");
            }

            GameData newGame = gameDAO.createGame(request);
            return new CreateGameResult(newGame.gameID());
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
