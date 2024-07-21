package RequestsResults;

public class RegisterResult {
    private final String message;
    private final String username;
    private final String authToken;


    public RegisterResult(String message) {
        this.message = message;
        this.username = null;
        this.authToken = null;
    }
    public RegisterResult(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
        this.message = null;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }

}
