package dataaccess;

import requestsresults.RegisterRequest;
import model.UserData;

public interface UserDAO {
    void clear() throws DataAccessException;

    void addUser(RegisterRequest request) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;
}
