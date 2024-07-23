package handlers;

import requestsresults.LogoutRequest;
import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import spark.*;

public class LogoutUserHandler extends Handler{
    final LogoutRequest logoutRequest;

    public LogoutUserHandler(Request request) {
        this.logoutRequest = new LogoutRequest(request.headers("authorization"));
    }

    public String logout() throws DataAccessException {
        try {
            USER_SERVICE.logoutUser(logoutRequest);
            return gson.toJson(new JsonObject());
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }


    }
}
