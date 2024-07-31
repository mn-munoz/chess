package dataaccess;

import requestsresults.CreateGameRequest;
import model.GameData;
import model.GameSummary;

import java.util.Collection;

public interface GameDAO {
    GameData createGame(CreateGameRequest request);

    GameData getGame(int gameID);

    Collection<GameSummary> listGames();

    void updateGame(int gameID, GameData game);

    void clear();
}
