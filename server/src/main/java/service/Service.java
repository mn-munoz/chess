package service;

import dataaccess.DataAccessException;
import dataaccess.memoryaccess.MemoryAuthDAO;
import dataaccess.memoryaccess.MemoryGameDAO;
import dataaccess.memoryaccess.MemoryUserDAO;
import model.AuthData;


public abstract class Service {
    protected static final MemoryAuthDAO authDAO = new MemoryAuthDAO();
    protected static final MemoryUserDAO userDAO = new MemoryUserDAO();
    protected static final MemoryGameDAO gameDAO = new MemoryGameDAO();

    public abstract void clear();

    public AuthData validateToken(String auth) throws DataAccessException {
        try {
            return authDAO.getAuth(auth);
        } catch (DataAccessException e) {
            throw new DataAccessException("unauthorized");
        }
    }
}
