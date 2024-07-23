package handlers;

import com.google.gson.Gson;
import service.AuthService;
import service.GameService;
import service.UserService;

public class Handler {
    protected static final UserService userService = new UserService();
    protected static final AuthService authService = new AuthService();
    protected static final GameService gameService = new GameService();
    protected final Gson gson = new Gson();

}
