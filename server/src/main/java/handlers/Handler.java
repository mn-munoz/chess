package handlers;

import service.AuthService;
import service.GameService;
import service.UserService;

public class Handler {
    protected static UserService userService = new UserService();
    protected static AuthService authService = new AuthService();
    protected static GameService gameService = new GameService();


}
