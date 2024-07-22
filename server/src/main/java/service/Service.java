package service;

import dataaccess.memoryaccess.MemoryAuthDAO;
import dataaccess.memoryaccess.MemoryGameDAO;
import dataaccess.memoryaccess.MemoryUserDAO;

public abstract class Service {
    protected static MemoryAuthDAO authDAO = new MemoryAuthDAO();
    protected static MemoryUserDAO userDAO = new MemoryUserDAO();
    protected static MemoryGameDAO gameDAO = new MemoryGameDAO();

    public abstract void clear();
}
