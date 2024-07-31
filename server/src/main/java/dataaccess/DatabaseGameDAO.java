package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.GameSummary;
import requestsresults.CreateGameRequest;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class DatabaseGameDAO extends DatabaseConnection implements GameDAO {
    public DatabaseGameDAO() throws DataAccessException {
        super();
    }

    @Override
    public GameData createGame(CreateGameRequest request) throws DataAccessException{
        String statement = "INSERT INTO games_table (gameName, whiteUsername, blackUsername, chessGame) VALUES (?, ?, ?, ?)";
        Gson gson = new Gson();
        String chessGameJson = gson.toJson(new ChessGame());

        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                ps.setString(1, request.gameName());
                ps.setString(2, null);
                ps.setString(3, null);
                ps.setString(4, chessGameJson);
                ps.executeUpdate();

                try (var rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int gameId = rs.getInt(1);
                        return new GameData(gameId, request.gameName(), null, null, gson.fromJson(chessGameJson, ChessGame.class));
                    } else {
                        throw new DataAccessException("Failed to retrieve the game ID");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException{
        String statement = "SELECT gameName, whiteUsername, blackUsername, chessGame FROM games_table WHERE gameID = ?";
        Gson gson = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String gameName = rs.getString("gameName");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String chessGame = rs.getString("chessGame");
                        return new GameData(gameID, gameName, whiteUsername, blackUsername, gson.fromJson(chessGame, ChessGame.class));
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }

    @Override
    public Collection<GameSummary> listGames() {
        return List.of();
    }

    @Override
    public void updateGame(int gameID, GameData game) {

    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "DELETE FROM games_table";
        try(var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }
}
