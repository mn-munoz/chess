package handlers;
import RequestsResults.RegisterRequest;
import RequestsResults.RegisterResult;
import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import model.AuthData;
import spark.*;


public class RegisterUserHandler extends Handler{
    private final RegisterRequest registerRequest;

    public RegisterUserHandler(Request request) {
        this.registerRequest = gson.fromJson(request.body(), RegisterRequest.class);
    }

    public String register() throws DataAccessException {
        try {
            return gson.toJson(userService.registerUser(registerRequest));
        }
        catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

}
