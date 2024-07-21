package handlers;

import RequestsResults.RegisterRequest;
import RequestsResults.RegisterResult;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import service.AuthService;
import service.UserService;

public class RegisterUserHandler {
    private final RegisterRequest request;
    private final AuthService authService;
    private final UserService userService;

    public RegisterUserHandler(String request,UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
        Gson gson = new Gson();
        this.request = gson.fromJson(request, RegisterRequest.class);
    }

    public RegisterResult register() {
        try {
            userService.reqisterUser(request);
            AuthData newToken = authService.createAuth(request.username());
            return new RegisterResult(request.username(), newToken.authToken());
        }
        catch (DataAccessException e) {
            return new RegisterResult(e.getMessage());
        }
    }

}
