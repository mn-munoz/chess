package handlers;

import com.google.gson.Gson;
import service.AuthService;
import service.GameService;
import service.UserService;

public class Handler {
    protected static final UserService USER_SERVICE = new UserService();
    protected static final AuthService AUTH_SERVICE = new AuthService();
    protected static final GameService GAME_SERVICE = new GameService();
    protected final Gson gson = new Gson();

}
