package service;

import org.junit.jupiter.api.*;
import requestsresults.*;
import dataaccess.DataAccessException;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    private final UserService userService = new UserService();

    // User Service Tests


    @AfterEach
    public void cleanServices() {
        userService.clear();
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

    // Game Service test

        // Not authorized to list games

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
