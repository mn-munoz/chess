package handlers;

import RequestsResults.LogoutRequest;
import com.google.gson.Gson;
import service.AuthService;
import service.UserService;
import spark.*;

public class LogoutUserHandler {
    private LogoutRequest logoutRequest;
    private AuthService authService;
    private UserService userService;

    public LogoutUserHandler(Request request, AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
        Gson gson = new Gson();
        this.logoutRequest = new LogoutRequest(request.headers("authorization"));
    }

    public void logout(LogoutRequest request, AuthService authService) {

    }
}
