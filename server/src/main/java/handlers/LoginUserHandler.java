package handlers;

import RequestsResults.LoginRequest;
import RequestsResults.LoginResult;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import service.AuthService;
import service.UserService;

public class LoginUserHandler {
    private final LoginRequest request;
    private final AuthService authService;
    private final UserService userService;

    public LoginUserHandler(String request,UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
        Gson gson = new Gson();
        this.request = gson.fromJson(request, LoginRequest.class);
    }

    public LoginResult login() {
        try {
            Gson gson = new Gson();
            userService.loginUser(request);
            AuthData newAuth = authService.createAuth(request.username());
            return new LoginResult(newAuth.username(), newAuth.authToken());

        }
        catch (DataAccessException e) {
            return new LoginResult(e.getMessage());
        }
    }
}
