package service;

import RequestsResults.RegisterRequest;
import dataaccess.DataAccessException;
import dataaccess.memoryaccess.MemoryUserDAO;

public class UserService {
    MemoryUserDAO userDAO = new MemoryUserDAO();
    public void clear() {
        userDAO.clear();
    }

    public void reqisterUser(RegisterRequest request) throws DataAccessException {
        if (userDAO.getUser(request.username()) != null) {
            throw new DataAccessException("Error: already taken");
        }
        userDAO.createUser(request);
    }
}
