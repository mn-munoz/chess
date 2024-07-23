package dataaccess;

import requestsResults.RegisterRequest;
import model.UserData;

public interface UserDAO {
    void clear();

    void addUser(RegisterRequest request);

    UserData getUser(String username);
}
