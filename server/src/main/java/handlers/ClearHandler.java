package handlers;

import service.AuthService;
import service.GameService;
import service.UserService;

public class ClearHandler {
    private final UserService userService;
    private final GameService gameService;
    private final AuthService authService;

    public ClearHandler(UserService userService, GameService gameService, AuthService authService) {
        this.userService = userService;
        this.gameService = gameService;
        this.authService = authService;
    }

    public void clean() {
        userService.clear();
        gameService.clear();
        authService.clear();
    }
}
