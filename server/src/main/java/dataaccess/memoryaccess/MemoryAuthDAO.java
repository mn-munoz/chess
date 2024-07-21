package dataaccess.memoryaccess;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {
    private final Map<String, AuthData> authMap = new HashMap<>();

    public void createAuth(AuthData auth){
        authMap.put(auth.authToken(), auth);
    }

    public AuthData getAuth(AuthData auth) throws DataAccessException {
        if(!authMap.containsKey(auth.authToken())) {
            throw new DataAccessException("Authorization token not found");
        }
        else {
            return authMap.get(auth.authToken());
        }
    }

    public void deleteAuth(AuthData auth) throws DataAccessException {
        if(!authMap.containsKey(auth.authToken())) {
            throw new DataAccessException("Authorization token not found");
        }
        else {
            authMap.remove(auth.authToken());
        }
    }

    public void clear() {
        authMap.clear();
    }


}
