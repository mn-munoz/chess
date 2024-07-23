package handlers;

import com.google.gson.JsonObject;

public class ClearHandler extends Handler {

    public JsonObject cleanService() {
        USER_SERVICE.clear();
        AUTH_SERVICE.clear();
        GAME_SERVICE.clear();
        return new JsonObject();
    }
}
