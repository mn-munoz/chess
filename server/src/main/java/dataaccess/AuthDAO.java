package dataaccess;

import model.AuthData;

public interface AuthDAO {
    public void createAuth(AuthData auth);

    public AuthData getAuth(AuthData auth) throws DataAccessException;

    public void deleteAuth(AuthData auth) throws DataAccessException;
}
