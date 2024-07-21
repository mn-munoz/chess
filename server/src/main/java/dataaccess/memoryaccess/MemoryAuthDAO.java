package dataaccess.memoryaccess;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private final Map<String, AuthData> authMap = new HashMap<>();

    // change this function so that it only inserts to the map. All other logic is applied in AuthService
    public AuthData createAuth(String username){
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);
        authMap.put(authToken, newAuth);
        return newAuth;
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
