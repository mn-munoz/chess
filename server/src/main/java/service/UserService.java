package service;

import dataaccess.UserDAO;
import dataaccess.memoryaccess.MemoryUserDAO;

public class UserService {
    MemoryUserDAO userDAO = new MemoryUserDAO();
    public void clear() {
    }
}
