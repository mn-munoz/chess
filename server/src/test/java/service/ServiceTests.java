package service;

import org.junit.jupiter.api.*;
import requestsresults.*;
import dataaccess.DataAccessException;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    private final UserService USER_SERVICE = new UserService();
    private final GameService GAME_SERVICE = new GameService();

    // User Service Tests


    @AfterEach
    public void cleanServices() {
        USER_SERVICE.clear();
        GAME_SERVICE.clear();
    }

    @Test
         // Successful Register
    public void successfulRegistration() throws DataAccessException {
        RegisterRequest request =
                new RegisterRequest("user", "12345", "user@gmail.com");
        RegisterResult result = USER_SERVICE.registerUser(request);
        RegisterResult expectedResult = new RegisterResult("user", result.authToken());

        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

        // Request is not valid

    @Test
    public void registrationNotValid() {
        RegisterRequest request = new RegisterRequest(null, "123", "asd@gmail.com");
        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                USER_SERVICE.registerUser(request));

        assertEquals("Error: bad request", exception.getMessage());
    }

        // User already taken

    @Test
    public void userAlreadyTaken() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("user", "123", "asd@gmail.com");
        USER_SERVICE.registerUser(request);
        DataAccessException exception = assertThrows(DataAccessException.class, () ->
            USER_SERVICE.registerUser(request));

        assertEquals("Error: already taken", exception.getMessage());
    }

        // Successful Login

    @Test
    public void successfulLogin() throws DataAccessException {
        RegisterRequest existingUser = new RegisterRequest("user", "123", "asd@gmail.com");
        USER_SERVICE.registerUser(existingUser);
        LoginRequest request = new LoginRequest("user", "123");
        LoginResult result = USER_SERVICE.loginUser(request);

        assertNotNull(result);
        assertEquals("user", Service.AUTH_DAO.getAuth(result.authToken()).username());
    }

        // wrong password
    @Test
    public void wrongPasswordLogin() throws DataAccessException {
        RegisterRequest existingUser = new RegisterRequest("user", "123", "asd@gmail.com");
        USER_SERVICE.registerUser(existingUser);
        LoginRequest request = new LoginRequest("user", "1234");
        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                USER_SERVICE.loginUser(request));

        assertNotNull(exception);
        assertEquals("Error: unauthorized", exception.getMessage());

    }

        // non existent user
    @Test
    public void loginNonExistentUser() {
        LoginRequest request = new LoginRequest("user", "123");
        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                USER_SERVICE.loginUser(request));

        assertNotNull(exception);
        assertEquals("Error: unauthorized", exception.getMessage());

    }
        // not authorized user

    @Test
    public void unauthorizedUserLogout() {
        LogoutRequest request = new LogoutRequest(null);

        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                USER_SERVICE.logoutUser(request));

        assertNotNull(exception);
        assertEquals("Error: unauthorized", exception.getMessage());
    }
        // successful logout

    @Test
    public void successfulLogout() throws DataAccessException {
        RegisterRequest existingUser = new RegisterRequest("user", "123", "asd@gmail.com");
        RegisterResult regResult = USER_SERVICE.registerUser(existingUser);
        String authToken = Service.AUTH_DAO.getAuth(regResult.authToken()).authToken();

        LogoutRequest request = new LogoutRequest(authToken);
        assertDoesNotThrow(() -> {
            USER_SERVICE.logoutUser(request);
        }, "Error: unauthorized");
    }

    // Game Service test

        // Not authorized to list games

    @Test
    public void unauthorizedListGames() {
        ListGamesRequest request = new ListGamesRequest(null);

        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                GAME_SERVICE.listGames(request));

        assertNotNull(exception);
        assertEquals("Error: unauthorized", exception.getMessage());
    }

        // successful listing of games



        // not authorized to create games

        // create game successfully

        // bad request to create game

        // missing player color in request

        // No game with id given

        // player color is not correct

        // White user is NOT taken

        // White User IS taken

        // Black user is NOT taken

        // Black user IS taken



}
