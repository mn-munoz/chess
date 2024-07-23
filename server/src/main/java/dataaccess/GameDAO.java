package dataaccess;

import RequestsResults.CreateGameRequest;
import model.GameData;
import model.GameSummary;

import java.util.Collection;

public interface GameDAO {
    GameData createGame(CreateGameRequest request);

    GameData getGame(int gameID) throws DataAccessException;

    Collection<GameSummary> listGames();

    void updateGame(int gameID, GameData game);
}
