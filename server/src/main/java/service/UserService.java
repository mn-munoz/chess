package service;

import requestsresults.*;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

public class UserService extends Service{
    public void clear() {
        USER_DAO.clear();
    }

    public RegisterResult registerUser(RegisterRequest request) throws DataAccessException {
        if (!isValidRequest(request)) {
            throw new DataAccessException("Error: bad request");
        }

        if (USER_DAO.getUser(request.username()) != null) {
            throw new DataAccessException("Error: already taken");
        }
        USER_DAO.addUser(request);
        AuthData newAuth = AUTH_DAO.createAuth(request.username());
        return new RegisterResult(request.username(), newAuth.authToken());
    }

    private boolean isValidRequest(RegisterRequest request) {
        return request.username() != null && request.password() != null && request.email() != null;
    }

    public LoginResult loginUser(LoginRequest request) throws DataAccessException {
        UserData user = USER_DAO.getUser(request.username());
        if (user == null || !(user.password().equals(request.password())) ) {
            throw new DataAccessException("Error: unauthorized");
        }

        AuthData authData = AUTH_DAO.createAuth(request.username());
        return new LoginResult(request.username(), authData.authToken());
    }

    public void logoutUser(LogoutRequest request) throws DataAccessException {
        try {
            AuthData tokenToDelete = validateToken(request.authToken());
            AUTH_DAO.deleteAuth(tokenToDelete.authToken());
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


}
