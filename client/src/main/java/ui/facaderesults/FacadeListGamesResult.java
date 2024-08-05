package ui.facaderesults;

import model.GameSummary;

import java.util.Collection;

public record FacadeListGamesResult(Collection<GameSummary> games) {
}
