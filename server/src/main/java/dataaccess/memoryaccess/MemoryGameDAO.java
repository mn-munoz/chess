package dataaccess.memoryaccess;

import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;

import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, GameData> authMap = new HashMap<>();

    public void createGame() {

    }

    public void getGame(){

    }

    public void listGames(){

    }

    public void updateGame(){

    }

    public void clear() {
        authMap.clear();
    }

}
