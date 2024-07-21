package service;

import dataaccess.memoryaccess.MemoryAuthDAO;
import model.AuthData;

public class AuthService {
    MemoryAuthDAO memoryDAO = new MemoryAuthDAO();

    public void clear() {
        memoryDAO.clear();
    }

    public AuthData createAuth(String username) {
        return memoryDAO.createAuth(username);
    }

}
