package service;

import model.AuthData;
import org.junit.jupiter.api.*;
import requestsresults.*;
import dataaccess.DataAccessException;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    private final UserService userService = new UserService();
    private GameService gameService = new GameService();
    private final AuthService authService = new AuthService();

    // Auth Service Tests

    @BeforeEach
    public void setUp() {
        cleanServices();
    }

    @AfterEach
    public void cleanServices() {
        userService.clear();
        gameService = new GameService();
        authService.clear();
    }

    @Test
    public void testAuthClear() throws DataAccessException {
        // Add some auth data to the authMap

        AuthData authData1 = Service.AUTH_DAO.createAuth("user1");
        AuthData authData2 = Service.AUTH_DAO.createAuth("user2");

        // Ensure that authMap contains the added auth data
        assertNotNull(Service.AUTH_DAO.getAuth(authData1.authToken()));
        assertNotNull(Service.AUTH_DAO.getAuth(authData2.authToken()));

        // Clear the authMap
        authService.clear();

        // Ensure that authMap is empty
        assertThrows(DataAccessException.class, () -> Service.AUTH_DAO.getAuth(authData1.authToken()));
        assertThrows(DataAccessException.class, () -> Service.AUTH_DAO.getAuth(authData2.authToken()));
    }

    // User Service Tests

        // Clear

    @Test
    public void testUserClear() throws DataAccessException {
        // Add some user data to the authMap
        RegisterRequest newUser =
                new RegisterRequest("user", "12345", "user@gmail.com");
        userService.registerUser(newUser);
        LoginRequest request = new LoginRequest("user", "12345");


        // Ensure that userMap contains the added auth data
        assertNotNull(Service.USER_DAO.getUser(newUser.username()));

        // Clear the userMap
        userService.clear();

        // Ensure that authMap is empty
        assertThrows(DataAccessException.class, () -> userService.loginUser(request));
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

    // Clear

    @Test
    public void testGameClear() throws DataAccessException {
        // Add some game data to the authMap
        RegisterRequest newUser =
                new RegisterRequest("user", "12345", "user@gmail.com");
        RegisterResult newUserResult = userService.registerUser(newUser);
        CreateGameRequest newGame = new CreateGameRequest(newUserResult.authToken(), "NewGame");
        CreateGameResult newGameResult = gameService.createGame(newGame);
        JoinGameRequest request = new JoinGameRequest(newUserResult.authToken(), "WHITE", newGameResult.gameID());

        // Ensure that gameMap contains the added game data
        assertNotNull(Service.GAME_DAO.getGame(newGameResult.gameID()));

        // Clear the gameMap
        gameService.clear();

        // Ensure that authMap is empty
        assertThrows(DataAccessException.class, () -> gameService.joinGame(request));
    }

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

    @Test
    public void successfulCreateGame() throws DataAccessException {
        RegisterRequest existingUser = new RegisterRequest("user", "123", "asd@gmail.com");
        RegisterResult regResult = userService.registerUser(existingUser);
        String authToken = Service.AUTH_DAO.getAuth(regResult.authToken()).authToken();

        CreateGameRequest request = new CreateGameRequest(authToken, "HelloGame");
        CreateGameResult result = gameService. createGame(request);

        assertNotNull(result);
        assertNotNull(Service.GAME_DAO.getGame(result.gameID()));
    }

        // bad request to create game

    @Test
    public void badRequestCreateGame() throws DataAccessException {
        RegisterRequest existingUser = new RegisterRequest("user", "123", "asd@gmail.com");
        RegisterResult regResult = userService.registerUser(existingUser);
        String authToken = Service.AUTH_DAO.getAuth(regResult.authToken()).authToken();

        CreateGameRequest request = new CreateGameRequest(authToken, null);
        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                gameService.createGame(request));

        assertEquals("Error: bad request", exception.getMessage());
    }

        // missing player color in request

    @Test
    public void nullPlayerColor() throws DataAccessException {
        RegisterRequest existingUser = new RegisterRequest("user", "123", "asd@gmail.com");
        RegisterResult regResult = userService.registerUser(existingUser);
        String authToken = Service.AUTH_DAO.getAuth(regResult.authToken()).authToken();

        CreateGameRequest createGame = new CreateGameRequest(authToken, "HelloGame");
        gameService. createGame(createGame);

        JoinGameRequest request = new JoinGameRequest(authToken, null, 1000);
        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                gameService.joinGame(request));

        assertEquals("Error: bad request", exception.getMessage());
    }

        // No game with id given

    @Test
    public void joinInvalidId() throws DataAccessException {
        RegisterRequest existingUser = new RegisterRequest("user", "123", "asd@gmail.com");
        RegisterResult regResult = userService.registerUser(existingUser);
        String authToken = Service.AUTH_DAO.getAuth(regResult.authToken()).authToken();

        CreateGameRequest createGame = new CreateGameRequest(authToken, "HelloGame2");
        gameService. createGame(createGame);

        JoinGameRequest request = new JoinGameRequest(authToken, "WHITE", 1100);
        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                gameService.joinGame(request));

        assertEquals("Error: bad request", exception.getMessage());
    }

        // player color is not correct

    @Test
    public void joinInvalidColor() throws DataAccessException {
        RegisterRequest existingUser = new RegisterRequest("user", "123", "asd@gmail.com");
        RegisterResult regResult = userService.registerUser(existingUser);
        String authToken = Service.AUTH_DAO.getAuth(regResult.authToken()).authToken();

        CreateGameRequest createGame = new CreateGameRequest(authToken, "HelloGame");
        gameService. createGame(createGame);

        JoinGameRequest request = new JoinGameRequest(authToken, "BLUE", 1000);
        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                gameService.joinGame(request));

        assertEquals("Error: bad request", exception.getMessage());
    }

        // White user is NOT taken

    @Test
    public void successfulJoinAsWhite() throws DataAccessException {
        RegisterRequest existingUser = new RegisterRequest("user", "123", "asd@gmail.com");
        RegisterResult regResult = userService.registerUser(existingUser);
        String authToken = Service.AUTH_DAO.getAuth(regResult.authToken()).authToken();

        CreateGameRequest createGame = new CreateGameRequest(authToken, "HelloGame");
        CreateGameResult result = gameService. createGame(createGame);
        JoinGameRequest request = new JoinGameRequest(authToken, "WHITE", result.gameID());

        assertDoesNotThrow(() -> gameService.joinGame(request), "Error: already taken");
    }

        // White User IS taken

    @Test
    public void whiteIsTaken() throws DataAccessException {
        RegisterRequest existingUserOne = new RegisterRequest("user", "123", "asd@gmail.com");
        RegisterResult regResultOne = userService.registerUser(existingUserOne);
        String authToken = Service.AUTH_DAO.getAuth(regResultOne.authToken()).authToken();

        RegisterRequest existingUserTwo = new RegisterRequest("testing", "123", "jkl@gmail.com");
        userService.registerUser(existingUserTwo);

        CreateGameRequest createGame = new CreateGameRequest(authToken, "HelloGame");
        gameService. createGame(createGame);
        JoinGameRequest addToWhite = new JoinGameRequest(authToken, "WHITE", 1000);
        gameService.joinGame(addToWhite);
        JoinGameRequest request = new JoinGameRequest(authToken, "WHITE", 1000);

        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                gameService.joinGame(request));

        assertEquals("Error: already taken", exception.getMessage());
    }

        // Black user is NOT taken

    @Test
    public void successfulJoinAsBlack() throws DataAccessException {
        RegisterRequest existingUser = new RegisterRequest("user", "123", "asd@gmail.com");
        RegisterResult regResult = userService.registerUser(existingUser);
        String authToken = Service.AUTH_DAO.getAuth(regResult.authToken()).authToken();

        CreateGameRequest createGame = new CreateGameRequest(authToken, "HelloGame");
        CreateGameResult result = gameService. createGame(createGame);
        JoinGameRequest request = new JoinGameRequest(authToken, "BLACK", result.gameID());

        assertDoesNotThrow(() -> gameService.joinGame(request), "Error: already taken");
    }

        // Black user IS taken

    @Test
        public void blackIsTaken() throws DataAccessException {
            RegisterRequest existingUserOne = new RegisterRequest("user", "123", "asd@gmail.com");
            RegisterResult regResultOne = userService.registerUser(existingUserOne);
            String authToken = Service.AUTH_DAO.getAuth(regResultOne.authToken()).authToken();

            RegisterRequest existingUserTwo = new RegisterRequest("testing", "123", "jkl@gmail.com");
            userService.registerUser(existingUserTwo);

            CreateGameRequest createGame = new CreateGameRequest(authToken, "HelloGame");
            gameService. createGame(createGame);
            JoinGameRequest addToWhite = new JoinGameRequest(authToken, "BLACK", 1000);
            gameService.joinGame(addToWhite);
            JoinGameRequest request = new JoinGameRequest(authToken, "BLACK", 1000);

            DataAccessException exception = assertThrows(DataAccessException.class, () ->
                    gameService.joinGame(request));

            assertEquals("Error: already taken", exception.getMessage());
        }



}
