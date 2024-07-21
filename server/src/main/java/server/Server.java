package server;

import RequestsResults.LoginResult;
import RequestsResults.RegisterRequest;
import RequestsResults.RegisterResult;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import handlers.ClearHandler;
import handlers.LoginUserHandler;
import handlers.RegisterUserHandler;
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
//        Spark.init();

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
        Gson gson = new Gson();

        Spark.delete("/db", (request, response) -> {
            ClearHandler clearHandler = new ClearHandler(userService, gameService, authService);
            clearHandler.clean();
            response.status(200);
            return gson.toJson(new Object());
        });

        Spark.post("/user", (request, response) -> {
            RegisterUserHandler registerHandler = new RegisterUserHandler(request.body(), userService, authService);
            RegisterResult result = registerHandler.register();

            if (result.getUsername() != null) {
                response.status(200);
            } else {
                response.status(403);
            }
            return gson.toJson(result);
        });

        Spark.post("/session", ((request, response) -> {
            LoginUserHandler loginHandler = new LoginUserHandler(request.body(), userService, authService);
            LoginResult result = loginHandler.login();

            if (result.getAuthToken() != null) {
                response.status(200);
            } else {
                response.status(401);
            }
            return gson.toJson(result);
        }));

        Spark.delete("/session", ((request, response) -> {
            response.status(200);
            System.out.println("hello");
            return gson.toJson(new Object());
        }));

        Spark.get("/game", ((request, response) -> {
            response.status(200);
            System.out.println("hello");
            return gson.toJson(new Object());
        }));

        Spark.post("/game", ((request, response) -> {
            response.status(200);
            System.out.println("hello");
            return gson.toJson(new Object());
        }));

        Spark.put("/game", ((request, response) -> {
            response.status(200);
            System.out.println("hello");
            return gson.toJson(new Object());
        }));
    }
}
