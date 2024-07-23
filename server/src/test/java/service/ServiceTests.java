package service;

import org.junit.jupiter.api.*;
import requestsresults.RegisterRequest;
import requestsresults.RegisterResult;
import service.UserService;
import dataaccess.DataAccessException;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    private final UserService userService = new UserService();

    // User Service Tests

//    @Test
        // Successful Register
//    public void successfulRegistration() throws DataAccessException {
//        RegisterRequest request =
//                new RegisterRequest("user", "12345", "user@gmail.com");
//        RegisterResult result = userService.registerUser(request);
//        RegisterResult expectedResult = new RegisterResult("user", )
//    }

        // Request is not valid

    @Test
    public void registrationNotValid() {
        RegisterRequest request = new RegisterRequest(null, "123", "asd@gmail.com");
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            userService.registerUser(request);
        });

        assertEquals("Error: bad request", exception.getMessage());
    }

        // Request not valid

        // User already taken

        // Successful Login

        // wrong password

        // non existent user

        // not authorized user

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
