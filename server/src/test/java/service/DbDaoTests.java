package service;

import dataaccess.DataAccessException;
import dataaccess.DatabaseUserDAO;
import org.junit.jupiter.api.Test;
import requestsresults.RegisterRequest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class DbDaoTests {

    @Test
    public void clearTest() throws DataAccessException {
        DatabaseUserDAO userDAO = new DatabaseUserDAO();

        assertDoesNotThrow(userDAO::clear);
    }

    @Test
    public void successfulAddUser() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("testUser", "123", "a@a.com");
        DatabaseUserDAO userDAO = new DatabaseUserDAO();

        assertDoesNotThrow(() -> userDAO.addUser(request));
    }

    public void failedAddUser() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("testUser", "123", "a@a.com");
        DatabaseUserDAO userDAO = new DatabaseUserDAO();

        assertDoesNotThrow(() -> userDAO.addUser(request));
    }

}
