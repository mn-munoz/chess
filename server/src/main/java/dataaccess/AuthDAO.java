package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(String username);

    AuthData getAuth(String auth) throws DataAccessException;

    void deleteAuth(String token);
}
