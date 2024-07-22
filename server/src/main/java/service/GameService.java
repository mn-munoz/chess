package service;

import dataaccess.memoryaccess.MemoryGameDAO;

public class GameService extends Service{

    public void clear() {
        gameDAO.clear();
    }
}
