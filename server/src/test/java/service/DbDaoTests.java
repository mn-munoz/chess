package service;

import dataaccess.DataAccessException;
import dataaccess.DatabaseUserDAO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class DbDaoTests {

    @Test
    public void clearTest() throws DataAccessException {

        DatabaseUserDAO userDAO = new DatabaseUserDAO();

        assertDoesNotThrow(userDAO::clear);
    }
}
