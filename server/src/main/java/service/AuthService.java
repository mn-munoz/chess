package service;

import dataaccess.memoryaccess.MemoryAuthDAO;

public class AuthService {
    MemoryAuthDAO memoryDAO = new MemoryAuthDAO();

    public void clear() {
        memoryDAO.clear();
    }

}
