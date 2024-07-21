package dataaccess;

import RequestsResults.RegisterRequest;
import model.UserData;

public interface UserDAO {
    public void clear();

    public void createUser(RegisterRequest request) throws DataAccessException;

    public UserData getUser(String username);
}
