package service;

import dataaccess.*;
import model.AuthData;


public abstract class Service {
    protected static final AuthDAO AUTH_DAO;
    protected static final UserDAO USER_DAO;
    protected static final GameDAO GAME_DAO;

    static {
        try {
            AUTH_DAO = new DatabaseAuthDAO();
            USER_DAO = new DatabaseUserDAO();
            GAME_DAO = new DatabaseGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public abstract void clear() throws DataAccessException;

    public AuthData validateToken(String auth) throws DataAccessException {
        try {
            return AUTH_DAO.getAuth(auth);
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: unauthorized");
        }
    }
}
