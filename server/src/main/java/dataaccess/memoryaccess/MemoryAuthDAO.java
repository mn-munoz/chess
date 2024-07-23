package dataaccess.memoryaccess;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private final Map<String, AuthData> authMap = new HashMap<>();

    public AuthData createAuth(String username){
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);
        authMap.put(authToken, newAuth);
        return newAuth;
    }

    public AuthData getAuth(String auth) throws DataAccessException {
        if(!authMap.containsKey(auth)) {
            throw new DataAccessException("Authorization token not found");
        }
        else {
            return authMap.get(auth);
        }
    }

    public void deleteAuth(String token)  {
            authMap.remove(token);
    }

    public void clear() {
        authMap.clear();
    }


}
