package handlers;

import com.google.gson.JsonObject;

public class ClearHandler extends Handler {

    public JsonObject cleanService() {
        userService.clear();
        authService.clear();
        gameService.clear();
        return new JsonObject();
    }
}
