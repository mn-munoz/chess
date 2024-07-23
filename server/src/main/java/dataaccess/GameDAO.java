package dataaccess;

import RequestsResults.CreateGameRequest;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    GameData createGame(CreateGameRequest request);

    void getGame();

    Collection<GameData> listGames();

    void updateGame();
}
