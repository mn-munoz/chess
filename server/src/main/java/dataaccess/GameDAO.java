package dataaccess;

import RequestsResults.CreateGameRequest;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    GameData createGame(CreateGameRequest request);

    GameData getGame(int gameID) throws DataAccessException;

    Collection<GameData> listGames();

    void updateGame(int gameID, GameData game);
}
