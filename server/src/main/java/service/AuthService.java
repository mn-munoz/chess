package service;

import RequestsResults.LogoutRequest;
import dataaccess.DataAccessException;
import dataaccess.memoryaccess.MemoryAuthDAO;
import model.AuthData;
import server.Server;


public class AuthService extends Service {

    public void clear() {
        authDAO.clear();
    }

    public AuthData createAuth(String username) {
        return authDAO.createAuth(username);
    }

    public boolean isAuthorized(String authToken) {
        try {
            return authDAO.getAuth(authToken) != null;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public void deleteToken(LogoutRequest request) {
        try {
            if (request.authToken() != null && authDAO.getAuth(request.authToken()) != null) {
                authDAO.deleteAuth(request.authToken());
            }
        }
        catch (DataAccessException e) {
            System.out.println("error");
        }
    }
}

