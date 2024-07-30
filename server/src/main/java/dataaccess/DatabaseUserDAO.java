package dataaccess;

import model.UserData;
import requestsresults.RegisterRequest;

import java.sql.SQLException;

public class DatabaseUserDAO extends DatabaseConnection implements UserDAO{

    public DatabaseUserDAO() throws DataAccessException{
        super();
    }


    @Override
    public void clear() throws DataAccessException{
        String statement = "TRUNCATE users_table";
        try(var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }

    @Override
    public void addUser(RegisterRequest request) {

    }

    @Override
    public UserData getUser(String username) {
        return null;
    }
}
