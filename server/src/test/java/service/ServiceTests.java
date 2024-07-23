package service;

import org.junit.jupiter.api.*;
import requestsresults.*;
import dataaccess.DataAccessException;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    private final UserService userService = new UserService();
    private final GameService gameService = new GameService();

    // User Service Tests


    @AfterEach
    public void cleanServices() {
        userService.clear();
        gameService.clear();
    }

    @Test
         // Successful Register
    public void successfulRegistration() throws DataAccessException {
        RegisterRequest request =
                new RegisterRequest("user", "12345", "user@gmail.com");
        RegisterResult result = userService.registerUser(request);
        RegisterResult expectedResult = new RegisterResult("user", result.authToken());

        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

        // Request is not valid

    @Test
    public void registrationNotValid() {
        RegisterRequest request = new RegisterRequest(null, "123", "asd@gmail.com");
        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                userService.registerUser(request));

        assertEquals("Error: bad request", exception.getMessage());
    }

        // User already taken

    @Test
    public void userAlreadyTaken() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("user", "123", "asd@gmail.com");
        userService.registerUser(request);
        DataAccessException exception = assertThrows(DataAccessException.class, () ->
            userService.registerUser(request));

        assertEquals("Error: already taken", exception.getMessage());
    }

        // Successful Login

    @Test
    public void successfulLogin() throws DataAccessException {
        RegisterRequest existingUser = new RegisterRequest("user", "123", "asd@gmail.com");
        userService.registerUser(existingUser);
        LoginRequest request = new LoginRequest("user", "123");
        LoginResult result = userService.loginUser(request);

        assertNotNull(result);
        assertEquals("user", Service.AUTH_DAO.getAuth(result.authToken()).username());
    }

        // wrong password
    @Test
    public void wrongPasswordLogin() throws DataAccessException {
        RegisterRequest existingUser = new RegisterRequest("user", "123", "asd@gmail.com");
        userService.registerUser(existingUser);
        LoginRequest request = new LoginRequest("user", "1234");
        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                userService.loginUser(request));

        assertNotNull(exception);
        assertEquals("Error: unauthorized", exception.getMessage());

    }

        // non existent user
    @Test
    public void loginNonExistentUser() {
        LoginRequest request = new LoginRequest("user", "123");
        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                userService.loginUser(request));

        assertNotNull(exception);
        assertEquals("Error: unauthorized", exception.getMessage());

    }
        // not authorized user

    @Test
    public void unauthorizedUserLogout() {
        LogoutRequest request = new LogoutRequest(null);

        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                userService.logoutUser(request));

        assertNotNull(exception);
        assertEquals("Error: unauthorized", exception.getMessage());
    }
        // successful logout

    @Test
    public void successfulLogout() throws DataAccessException {
        RegisterRequest existingUser = new RegisterRequest("user", "123", "asd@gmail.com");
        RegisterResult regResult = userService.registerUser(existingUser);
        String authToken = Service.AUTH_DAO.getAuth(regResult.authToken()).authToken();

        LogoutRequest request = new LogoutRequest(authToken);
        assertDoesNotThrow(() -> userService.logoutUser(request), "Error: unauthorized");
    }

    // Game Service test

        // Not authorized to list games

    @Test
    public void unauthorizedListGames() {
        ListGamesRequest request = new ListGamesRequest(null);

        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                gameService.listGames(request));

        assertNotNull(exception);
        assertEquals("Error: unauthorized", exception.getMessage());
    }

        // successful listing of games

    @Test
    public void successfulListingGames() throws DataAccessException {
        RegisterRequest existingUser = new RegisterRequest("user", "123", "asd@gmail.com");
        RegisterResult regResult = userService.registerUser(existingUser);
        String authToken = Service.AUTH_DAO.getAuth(regResult.authToken()).authToken();

        ListGamesRequest request = new ListGamesRequest(authToken);
        ListGamesResult result = gameService.listGames(request);

        assertNotNull(result);
    }
        // not authorized to create games

    @Test
    public void unauthorizedCreateGame() {
        CreateGameRequest request = new CreateGameRequest(null, "HelloGame");

        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                gameService.createGame(request));

        assertNotNull(exception);
        assertEquals("Error: unauthorized", exception.getMessage());
    }

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
