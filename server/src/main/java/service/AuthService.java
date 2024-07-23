package service;

public class AuthService extends Service {

    public void clear() {
        AUTH_DAO.clear();
    }
}

