package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(String username) throws DataAccessException;

    AuthData getAuth(String auth) throws DataAccessException;

    void deleteAuth(String token);

    void clear() throws DataAccessException;
}
