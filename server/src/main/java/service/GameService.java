package service;

import dataaccess.memoryaccess.MemoryGameDAO;

public class GameService {
    MemoryGameDAO gameDAO = new MemoryGameDAO();

    public void clear() {
        gameDAO.clear();
    }
}
