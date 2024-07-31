package dataaccess;

import model.GameData;
import model.GameSummary;
import requestsresults.CreateGameRequest;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class DatabaseGameDAO extends DatabaseConnection implements GameDAO {
    public DatabaseGameDAO() throws DataAccessException {
        super();
    }

    @Override
    public GameData createGame(CreateGameRequest request) {
        return null;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
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
