package handlers;

import com.google.gson.JsonObject;
import dataaccess.DataAccessException;

public class ClearHandler extends Handler {

    public JsonObject cleanService() throws DataAccessException {
        USER_SERVICE.clear();
        AUTH_SERVICE.clear();
        GAME_SERVICE.clear();
        return new JsonObject();
    }
}
