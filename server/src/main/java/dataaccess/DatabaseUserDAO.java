package dataaccess;

import com.google.gson.Gson;
import model.UserData;
import requestsresults.RegisterRequest;

import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUserDAO extends DatabaseConnection implements UserDAO{

    public DatabaseUserDAO() throws DataAccessException{
        super();
    }


    @Override
    public void clear() throws DataAccessException {
        String statement = "TRUNCATE users_table";
        try(var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }


    // remember to hash password
    @Override
    public void addUser(RegisterRequest request) throws DataAccessException {
        Gson gson = new Gson();
        String username = request.username();
        String userData = gson.toJson(request);

        String statement = "INSERT INTO users_table (username, userData) VALUES (?, ?)";

        try (var conn = DatabaseManager.getConnection()) {
             try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                 ps.setString(1, username);
                 ps.setString(2, userData);
                 ps.executeUpdate();
             }
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) {
        return null;
    }
}
