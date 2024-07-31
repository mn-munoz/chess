package java.dataaccess;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import requestsresults.CreateGameRequest;
import requestsresults.RegisterRequest;

import static org.junit.jupiter.api.Assertions.*;

public class DbDaoTests {

    @BeforeAll
    public static void setUp() throws DataAccessException {
        DatabaseUserDAO userDAO = new DatabaseUserDAO();
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();

        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }

    @Test
    public void clearTest() throws DataAccessException {
        DatabaseUserDAO userDAO = new DatabaseUserDAO();
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();


        assertDoesNotThrow(userDAO::clear);
        assertDoesNotThrow(authDAO::clear);
        assertDoesNotThrow(gameDAO::clear);
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

    @Test
    public void successfulGetAuth() throws DataAccessException {
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        RegisterRequest request = new RegisterRequest("testUser2",  "1234", "b@a.com");
        DatabaseUserDAO userDAO = new DatabaseUserDAO();
        userDAO.addUser(request);
        AuthData authData = authDAO.createAuth("testUser2");
        String token = authData.authToken();

        assertDoesNotThrow(() -> authDAO.getAuth(token));
    }

    @Test
    public void failedGetAuth() throws DataAccessException {
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();

        assertThrows(DataAccessException.class, () -> authDAO.getAuth("thisIsNotValidToken"));
    }

    @Test
    public void successfulDeleteAuth() throws DataAccessException {
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        RegisterRequest request = new RegisterRequest("anotherTestUser",  "1234", "b@a.com");
        DatabaseUserDAO userDAO = new DatabaseUserDAO();
        userDAO.addUser(request);
        AuthData authData = authDAO.createAuth("anotherTestUser");
        String token = authData.authToken();

        assertDoesNotThrow(() -> authDAO.deleteAuth(token));

    }

    @Test
    public void failedDeleteAuth() throws DataAccessException {
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();

        assertThrows(DataAccessException.class, () -> authDAO.deleteAuth("thisIsNotValidToken"));
    }

    @Test
    public void successfulCreateGame() throws DataAccessException {
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        RegisterRequest request = new RegisterRequest("createGameTest",  "1234", "b@a.com");
        DatabaseUserDAO userDAO = new DatabaseUserDAO();
        userDAO.addUser(request);
        AuthData authData = authDAO.createAuth("createGameTest");

        CreateGameRequest gameRequest = new CreateGameRequest(authData.authToken(), "testGame");

        assertDoesNotThrow(() -> gameDAO.createGame(gameRequest));
    }

    @Test
    public void failedCreateGame() throws DataAccessException {
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        RegisterRequest request = new RegisterRequest("createFailedGameTest",  "1234", "b@a.com");
        DatabaseUserDAO userDAO = new DatabaseUserDAO();
        userDAO.addUser(request);
        AuthData authData = authDAO.createAuth("createFailedGameTest");
        CreateGameRequest gameRequest = new CreateGameRequest(authData.authToken(), null);

        assertThrows(DataAccessException.class, () -> gameDAO.createGame(gameRequest));
    }

    @Test
    public void successfulGetGame() throws DataAccessException {
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        RegisterRequest request = new RegisterRequest("getGameTest",  "1234", "b@a.com");
        DatabaseUserDAO userDAO = new DatabaseUserDAO();
        userDAO.addUser(request);
        AuthData authData = authDAO.createAuth("getGameTest");
        CreateGameRequest gameRequest = new CreateGameRequest(authData.authToken(), "getGame");
        GameData game = gameDAO.createGame(gameRequest);

        assertNotNull((gameDAO.getGame(game.gameID())));
        assertDoesNotThrow(()-> gameDAO.getGame(game.gameID()));
    }

    @Test
    public void failedGetGame() throws DataAccessException {
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        RegisterRequest request = new RegisterRequest("getGameTest",  "1234", "b@a.com");
        DatabaseUserDAO userDAO = new DatabaseUserDAO();
        userDAO.addUser(request);
        AuthData authData = authDAO.createAuth("getGameTest");
        CreateGameRequest gameRequest = new CreateGameRequest(authData.authToken(), "getGame");
        gameDAO.createGame(gameRequest);

        assertNull((gameDAO.getGame(9999)));
    }
}
