package dataaccess;

import requestsresults.CreateGameRequest;
import model.GameData;
import model.GameSummary;

import java.util.Collection;

public interface GameDAO {
    GameData createGame(CreateGameRequest request) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    Collection<GameSummary> listGames() throws DataAccessException;

    void updateGame(int gameID, GameData game);

    void clear() throws DataAccessException;
}
