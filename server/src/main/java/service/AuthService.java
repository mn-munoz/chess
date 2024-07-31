package service;

import dataaccess.DataAccessException;

public class AuthService extends Service {

    public void clear() throws DataAccessException {
        AUTH_DAO.clear();
    }
}

