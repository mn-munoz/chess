package dataaccess;

import model.AuthData;

public interface AuthDAO {
    public AuthData createAuth(String username);

    public AuthData getAuth(AuthData auth) throws DataAccessException;

    public void deleteAuth(AuthData auth) throws DataAccessException;
}
