package RequestsResults;

public class RegisterResult {
    private final String username;
    private final String authToken;

    public RegisterResult(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }

}
