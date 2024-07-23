package server;

import RequestsResults.ErrorResult;
import RequestsResults.LoginResult;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import handlers.ClearHandler;
import handlers.LoginUserHandler;
import handlers.RegisterUserHandler;
import service.AuthService;
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
        AuthService authService = new AuthService();
        Gson gson = new Gson();
//        String unauthorizedJson = gson.toJson(new RegisterResult("Error:unauthorized"));
//
//        Spark.before("/session", "DELETE", (request, response) -> {
//            System.out.println(request.getClass());
//            String authToken = request.headers("authorization");
//            if (authToken == null || !authService.isAuthorized(authToken)) {
//                halt(401, unauthorizedJson);
//            }
//        });

//        Spark.before("/game", (request, response) -> {
//            String authToken = request.headers("authorization");
//            if (authToken == null || !authService.isAuthorized(authToken)) {
//                halt(401, unauthorizedJson);
//            }
//        });

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

        Spark.post("/user", (request, response) -> {
            try {
                RegisterUserHandler registerHandler = new RegisterUserHandler(request);
                response.status(200);
                return registerHandler.register();
            } catch (Exception e) {
                if (e instanceof  DataAccessException && e.getMessage().contains("bad request")) {
                    response.status(400);
                }
                else if (e instanceof  DataAccessException && e.getMessage().contains("already taken")) {
                    response.status(403);
                }
                else {
                    response.status(500);
                }
                return gson.toJson(new ErrorResult("Error: " + e.getMessage()));
            }


        });
//
        Spark.post("/session", ((request, response) -> {
            try {
                LoginUserHandler loginHandler = new LoginUserHandler(request);
                response.status(200);
                return loginHandler.login();
            } catch (Exception e) {
                if (e instanceof DataAccessException) {
                   response.status(401);
                }
                else {
                    response.status(500);
                }
                return gson.toJson(new ErrorResult("Error: " + e.getMessage()));
            }
        }));
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
