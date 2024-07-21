package server;

import com.google.gson.Gson;
import handlers.ClearHandler;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        createRoutes();

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void createRoutes(){
        UserService userService = new UserService();
        GameService gameService = new GameService();
        AuthService authService = new AuthService();
        ClearHandler clearHandler = new ClearHandler(userService, gameService, authService);

        Spark.delete("/db", (request, response) -> {
            Gson gson = new Gson();
            clearHandler.clean();
            response.status(200);
            response.type("application/json");
            return gson.toJson(new Object());
        });
        ;
    }
}
