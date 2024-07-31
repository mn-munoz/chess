package dataaccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.UUID;

public class DatabaseAuthDAO extends DatabaseConnection implements AuthDAO {

    public DatabaseAuthDAO() throws DataAccessException {
        super();
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        String statement = "INSERT INTO auth_table (authToken, username) VALUES (?, ?)";

        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                ps.setString(2, username);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }

        return authData;
    }

    @Override
    public AuthData getAuth(String auth) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String token) {

    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "DELETE FROM auth_table";
        try(var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }

}
