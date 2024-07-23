package dataaccess.memoryaccess;

import requestsresults.CreateGameRequest;
import chess.ChessGame;
import dataaccess.GameDAO;
import model.GameData;
import model.GameSummary;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, GameData> gamesMap = new HashMap<>();
    private final Map<Integer, GameSummary> gameSummary = new HashMap<>();
    private static int lastIDGiven = 1000;

    public GameData createGame(CreateGameRequest request) {
        GameData newGame = new GameData(lastIDGiven, null, null, request.gameName(), new ChessGame());
        GameSummary newSummary = new GameSummary(newGame.gameID(), newGame.whiteUsername(), newGame.blackUsername(), newGame.gameName());
        gamesMap.put(lastIDGiven, newGame);
        gameSummary.put(lastIDGiven, newSummary);
        increaseID();
        return newGame;
    }

    public GameData getGame(int gameID) {
        return gamesMap.getOrDefault(gameID, null);
    }

    public Collection<GameSummary> listGames(){
        return gameSummary.values();
    }

    public void updateGame(int gameID, GameData game){
        gamesMap.put(gameID, game);
        GameSummary newSummary = new GameSummary(gameID, game.whiteUsername(), game.blackUsername(), game.gameName());
        gameSummary.put(gameID, newSummary);
    }

    public void clear() {
        gamesMap.clear();
        gameSummary.clear();
    }

    private void increaseID() {
        lastIDGiven++;
    }

}
