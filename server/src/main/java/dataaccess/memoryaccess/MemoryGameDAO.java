package dataaccess.memoryaccess;

import RequestsResults.CreateGameRequest;
import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, GameData> gamesMap = new HashMap<>();
    private static int lastIDGiven = 1000;

    public GameData createGame(CreateGameRequest request) {
        GameData newGame = new GameData(lastIDGiven, null, null, request.gameName(), new ChessGame());
        gamesMap.put(lastIDGiven, newGame);
        increaseID();
        return newGame;
    }

    public GameData getGame(int gameID) {
        return gamesMap.getOrDefault(gameID, null);
    }


    public Collection<GameData> listGames(){
        return gamesMap.values();
    }

    public void updateGame(int gameID, GameData game){
        gamesMap.put(gameID, game);
    }

    public void clear() {
        gamesMap.clear();
    }

    private void increaseID() {
        lastIDGiven++;
    }

}
