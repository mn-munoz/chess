package service;

import requestsresults.*;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;

public class GameService extends Service{

    public void clear() throws DataAccessException {
        GAME_DAO.clear();
    }

    public ListGamesResult listGames(ListGamesRequest request) throws DataAccessException {
        try {
            validateToken(request.authToken());

            return new ListGamesResult(GAME_DAO.listGames());
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public CreateGameResult createGame(CreateGameRequest request) throws DataAccessException {
        try {
            validateToken(request.authToken());

            if (request.gameName() == null) {
                throw new DataAccessException("Error: bad request");
            }

            GameData newGame = GAME_DAO.createGame(request);
            return new CreateGameResult(newGame.gameID());
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


    public void joinGame(JoinGameRequest request) throws DataAccessException {
        try {
            AuthData data = validateToken(request.authToken());
            GameData game = GAME_DAO.getGame(request.gameID());
            if (request.playerColor() == null || game == null || !isValidColor(request.playerColor())) {
                throw new DataAccessException("Error: bad request");
            }

            GameData newGame = getGameData(request, game, data);

            GAME_DAO.updateGame(request.gameID(), newGame);

        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private boolean isValidColor(String playerColor) {
        return playerColor.equals("WHITE") || playerColor.equals("BLACK");
    }

    private GameData getGameData(JoinGameRequest request, GameData game, AuthData data) throws DataAccessException {

        GameData newGame = game;

        if (request.playerColor().equalsIgnoreCase("WHITE")) {
            if (game.whiteUsername() != null) {
                throw new DataAccessException("Error: already taken");
            }
            newGame = new GameData(game.gameID(), data.username(), game.blackUsername(), game.gameName(), game.game());
        }

        else if (request.playerColor().equalsIgnoreCase("BLACK")) {
            if (game.blackUsername() != null) {
                throw new DataAccessException("Error: already taken");
            }
            newGame = new GameData(game.gameID(), game.whiteUsername(), data.username(), game.gameName(), game.game());
        }
        return newGame;
    }
}
