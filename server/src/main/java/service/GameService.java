package service;

import RequestsResults.*;
import dataaccess.DataAccessException;
import model.AuthData;
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

    public void joinGame(JoinGameRequest request) throws DataAccessException {
        try {
            AuthData data = validateToken(request.authToken());

            GameData game = gameDAO.getGame(request.gameID());
            if (request.playerColor() == null || game == null) {
                throw new DataAccessException("bad request");
            }

            GameData newGame = getGameData(request, game, data);

            gameDAO.updateGame(request.gameID(), newGame);

        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private static GameData getGameData(JoinGameRequest request, GameData game, AuthData data) throws DataAccessException {
        GameData newGame = null;

        if (request.playerColor().equals("WHITE")) {
            if (game.whiteUsername() != null) {
                throw new DataAccessException("already taken");
            }
            newGame = new GameData(game.gameID(), data.username(), game.blackUsername(), game.gameName(), game.game());
        }

        if (request.playerColor().equals("BLACK")) {
            if (game.blackUsername() != null) {
                throw new DataAccessException("already taken");
            }
            newGame = new GameData(game.gameID(), game.whiteUsername(), data.username(), game.gameName(), game.game());
        }
        return newGame;
    }
}
