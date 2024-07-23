package service;

import dataaccess.DataAccessException;
import dataaccess.memoryaccess.MemoryAuthDAO;
import dataaccess.memoryaccess.MemoryGameDAO;
import dataaccess.memoryaccess.MemoryUserDAO;
import model.AuthData;


public abstract class Service {
    protected static final MemoryAuthDAO AUTH_DAO = new MemoryAuthDAO();
    protected static final MemoryUserDAO USER_DAO = new MemoryUserDAO();
    protected static final MemoryGameDAO GAME_DAO = new MemoryGameDAO();

    public abstract void clear();

    public AuthData validateToken(String auth) throws DataAccessException {
        try {
            return AUTH_DAO.getAuth(auth);
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: unauthorized");
        }
    }
}
