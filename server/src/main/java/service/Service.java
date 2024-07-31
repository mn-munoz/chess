package service;

import dataaccess.*;
import dataaccess.memoryaccess.MemoryGameDAO;
import model.AuthData;


public abstract class Service {
    protected static final AuthDAO AUTH_DAO;
    protected static final UserDAO USER_DAO;

    static {
        try {
            AUTH_DAO = new DatabaseAuthDAO();
            USER_DAO = new DatabaseUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected static final GameDAO GAME_DAO = new MemoryGameDAO();

    public abstract void clear() throws DataAccessException;

    public AuthData validateToken(String auth) throws DataAccessException {
        try {
            return AUTH_DAO.getAuth(auth);
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: unauthorized");
        }
    }
}
