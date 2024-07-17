import chess.*;
import spark.Spark;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        int port = 8080;
        Spark.port(port);
        Spark.staticFileLocation("/web");

        createRoutes();
        Spark.awaitInitialization();
        System.out.println("Listening on port " + port);

    }
    private static void createRoutes() {
        Spark.get("/hello", (req, res) -> "Hello BYU!");
    }
}