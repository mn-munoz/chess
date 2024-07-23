package requestsresults;

import java.util.Collection;

public record ListGamesResult(Collection<model.GameSummary> games) {
}
