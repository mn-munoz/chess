package service;

import RequestsResults.LoginRequest;
import RequestsResults.RegisterRequest;
import dataaccess.DataAccessException;
import dataaccess.memoryaccess.MemoryUserDAO;
import model.UserData;

public class UserService {
    MemoryUserDAO userDAO = new MemoryUserDAO();
    public void clear() {
        userDAO.clear();
    }

    public void registerUser(RegisterRequest request) throws DataAccessException {
        if (userDAO.getUser(request.username()) != null) {
            throw new DataAccessException("Error: already taken");
        }
        userDAO.createUser(request);
    }

    public void loginUser(LoginRequest request) throws DataAccessException {
        UserData user = userDAO.getUser(request.username());
        if (user == null || !(user.password().equals(request.password())) ) {
            throw new DataAccessException("Error: unauthorized");
        }

    }
}
