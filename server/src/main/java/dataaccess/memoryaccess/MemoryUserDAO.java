package dataaccess.memoryaccess;
import java.util.HashMap;
import java.util.Map;

import RequestsResults.RegisterRequest;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;

public class MemoryUserDAO implements UserDAO {
    private final Map<String, UserData> usersMap = new HashMap<>();

    public void clear(){
        usersMap.clear();
    }

    public void createUser(RegisterRequest request){
            usersMap.put(request.username(), new UserData(request.username(), request.password(), request.email()));
    }

    public UserData getUser(String username){
        return usersMap.getOrDefault(username,null);
    }
}
