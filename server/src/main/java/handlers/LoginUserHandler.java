package handlers;

import RequestsResults.LoginRequest;
import dataaccess.DataAccessException;
import spark.*;

public class LoginUserHandler extends Handler{
    final LoginRequest loginRequest;

    public LoginUserHandler(Request request) {
        this.loginRequest = gson.fromJson(request.body(), LoginRequest.class);
    }

    public String login() throws DataAccessException {
        try {
            return gson.toJson(userService.loginUser(loginRequest));
        }
        catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
