package service;

import dataaccess.DataAccessException;
import dataaccess.DatabaseAuthDAO;
import dataaccess.DatabaseUserDAO;
import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import requestsresults.RegisterRequest;

import static org.junit.jupiter.api.Assertions.*;

public class DbDaoTests {

    @AfterAll
    public static void setUp() throws DataAccessException {
        DatabaseUserDAO userDAO = new DatabaseUserDAO();
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();

        userDAO.clear();
        authDAO.clear();
    }

    @Test
    public void clearTest() throws DataAccessException {
        DatabaseUserDAO userDAO = new DatabaseUserDAO();
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();

        assertDoesNotThrow(userDAO::clear);
        assertDoesNotThrow(authDAO::clear);
    }

    @Test
    public void successfulAddUser() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("testUser", "123", "a@a.com");
        DatabaseUserDAO userDAO = new DatabaseUserDAO();

        assertDoesNotThrow(() -> userDAO.addUser(request));
    }

    @Test
    public void failedAddUser() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("testUser",  null, "a@a.com");
        DatabaseUserDAO userDAO = new DatabaseUserDAO();

        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                userDAO.addUser(request));

        assertEquals("Error: invalid request format", exception.getMessage());
    }

    @Test
    public void successfulGetUser() throws DataAccessException {
        String username = "testUser";
        DatabaseUserDAO userDAO = new DatabaseUserDAO();

        UserData user = assertDoesNotThrow(() -> userDAO.getUser(username));
        assertEquals(username, user.username());

    }

    @Test
    public void failedGetUser() throws DataAccessException {
        String username = "notInDatabase";
        DatabaseUserDAO userDAO = new DatabaseUserDAO();

        assertNull(userDAO.getUser(username));
    }


    @Test
    public void successfulCreateAuth() throws DataAccessException {
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        String username = "testUser";

        assertDoesNotThrow(() -> authDAO.createAuth(username));
    }

    @Test
    public void failedCreateAuth() throws DataAccessException {
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        String username = "notAUser";

        assertThrows(DataAccessException.class, () -> authDAO.createAuth(username));
    }


}
