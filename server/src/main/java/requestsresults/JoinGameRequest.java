package requestsresults;

public record JoinGameRequest(String authToken,String playerColor, int gameID) {
}
