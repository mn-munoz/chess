package dataaccess.memoryaccess;
import java.util.HashMap;
import java.util.Map;

import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

public class MemoryUserDAO implements UserDAO {
    private final Map<String, UserData> usersMap = new HashMap<>();

    public void clear(){
        usersMap.clear();
    }

    public void createUser(){


    }

    public void getUser(){

    }
}
