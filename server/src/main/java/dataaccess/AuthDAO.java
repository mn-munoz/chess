package dataaccess;

import model.AuthData;

public interface AuthDAO {
    public AuthData createAuth(String username);

    public AuthData getAuth(String auth) throws DataAccessException;

    public void deleteAuth(String token) throws DataAccessException;
}
