package RequestsResults;

import java.util.Collection;

public record ListGamesResult(Collection<model.GameSummary> games) {
}
