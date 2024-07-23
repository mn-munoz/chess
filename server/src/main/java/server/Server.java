package server;

import requestsresults.ErrorResult;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import handlers.*;
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
        Gson gson = new Gson();

        Spark.delete("/db", (request, response) -> {
            try {
                ClearHandler clearHandler = new ClearHandler();
                response.status(200);
                return clearHandler.cleanService();
            } catch (Exception e) {
                response.status(500);
                return gson.toJson(new ErrorResult(e.getMessage()));
            }

        });

        createUserEndpoints(gson);

        createGameEndpoints(gson);
   }

   private void createUserEndpoints(Gson gson) {
       Spark.post("/user", (request, response) -> {
           try {
               RegisterUserHandler registerHandler = new RegisterUserHandler(request);
               response.status(200);
               return registerHandler.register();
           } catch (Exception e) {
               if (e.getMessage().contains("bad request")) {
                   response.status(400);
               }
               else if (e.getMessage().contains("already taken")) {
                   response.status(403);
               }
               else {
                   response.status(500);
               }
               return gson.toJson(new ErrorResult(e.getMessage()));
           }


       });
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
               return gson.toJson(new ErrorResult(e.getMessage()));
           }
       }));

       Spark.delete("/session", (request, response) -> {
           try {
               LogoutUserHandler logoutHandler = new LogoutUserHandler(request);
               response.status(200);
               return logoutHandler.logout();
           } catch (Exception e) {
               if (e instanceof DataAccessException) {
                   response.status(401);
               }
               else {
                   response.status(500);
               }
               return gson.toJson(new ErrorResult(e.getMessage()));
           }
       });
   }

   private void createGameEndpoints(Gson gson){
       Spark.get("/game", ((request, response) -> {
           try {
               ListGamesHandler listGamesHandler = new ListGamesHandler(request);
               response.status(200);
               return listGamesHandler.listGames();
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

       Spark.post("/game", ((request, response) -> {
           try {
               CreateGameHandler createGameHandler = new CreateGameHandler(request);
               response.status(200);
               return createGameHandler.createGame();
           } catch (Exception e) {
               if (e.getMessage().contains("bad request")) {
                   response.status(400);
               }
               else if (e.getMessage().contains("unauthorized")) {
                   response.status(401);
               }
               else {
                   response.status(500);
               }
               return gson.toJson(new ErrorResult("Error: " + e.getMessage()));
           }
       }));

       Spark.put("/game", ((request, response) -> {
           try {
               JoinGameHandler joinGameHandler = new JoinGameHandler(request);
               response.status(200);
               return joinGameHandler.joinGame();
           } catch (Exception e) {
               if (e.getMessage().contains("bad request")) {
                   response.status(400);
               }
               else if (e.getMessage().contains("unauthorized")) {
                   response.status(401);
               }
               else if (e.getMessage().contains("taken")) {
                   response.status(403);
               }
               else {
                   response.status(500);
               }
               return gson.toJson(new ErrorResult("Error: " + e.getMessage()));
           }
       }));
   }
}
