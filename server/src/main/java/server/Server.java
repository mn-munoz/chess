package server;

import RequestsResults.ErrorResult;
import RequestsResults.LoginResult;
import RequestsResults.RegisterResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import handlers.ClearHandler;
import handlers.LoginUserHandler;
import handlers.RegisterUserHandler;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;

import static spark.Spark.halt;

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
        String unauthorizedJson = gson.toJson(new RegisterResult("Error:unauthorized"));

        Spark.before("/session", "DELETE", (request, response) -> {
            System.out.println(request.getClass());
            String authToken = request.headers("authorization");
            if (authToken == null || !authService.isAuthorized(authToken)) {
                halt(401, unauthorizedJson);
            }
        });

        Spark.before("/game", (request, response) -> {
            String authToken = request.headers("authorization");
            if (authToken == null || !authService.isAuthorized(authToken)) {
                halt(401, unauthorizedJson);
            }
        });

        Spark.delete("/db", (request, response) -> {
            try {
                ClearHandler clearHandler = new ClearHandler();
                response.status(200);
                return clearHandler.cleanService();
            } catch (Exception e) {
                response.status(500);
                return gson.toJson(new ErrorResult("Error: " + e.getMessage()));
            }

        });

//        Spark.post("/user", (request, response) -> {
//            RegisterUserHandler registerHandler = new RegisterUserHandler(request.body(), userService, authService);
//            RegisterResult result = registerHandler.register();
//
//            if (result.getUsername() != null) {
//                response.status(200);
//            } else {
//                response.status(403);
//            }
//            return gson.toJson(result);
//        });
//
//        Spark.post("/session", ((request, response) -> {
//            LoginUserHandler loginHandler = new LoginUserHandler(request.body(), userService, authService);
//            LoginResult result = loginHandler.login();
//
//            if (result.getAuthToken() != null) {
//                response.status(200);
//            } else {
//                response.status(401);
//            }
//            return gson.toJson(result);
//        }));
//
//        Spark.delete("/session", ((request, response) -> {
//            response.status(200);
//        }));
//
//        Spark.get("/game", ((request, response) -> {
//            response.status(200);
//            System.out.println("hello");
//            return gson.toJson(new Object());
//        }));
//
//        Spark.post("/game", ((request, response) -> {
//            response.status(200);
//            System.out.println("hello");
//            return gson.toJson(new Object());
//        }));
//
//        Spark.put("/game", ((request, response) -> {
//            response.status(200);
//            System.out.println("hello");
//            return gson.toJson(new Object());
//        }));
   }
}
