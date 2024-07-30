package dataaccess;

import requestsresults.RegisterRequest;
import model.UserData;

public interface UserDAO {
    void clear() throws DataAccessException;

    void addUser(RegisterRequest request);

    UserData getUser(String username);
}
