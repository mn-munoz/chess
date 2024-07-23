package handlers;
import requestsresults.RegisterRequest;
import dataaccess.DataAccessException;
import spark.*;


public class RegisterUserHandler extends Handler{
    private final RegisterRequest registerRequest;

    public RegisterUserHandler(Request request) {
        this.registerRequest = gson.fromJson(request.body(), RegisterRequest.class);
    }

    public String register() throws DataAccessException {
        try {
            return gson.toJson(USER_SERVICE.registerUser(registerRequest));
        }
        catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

}
