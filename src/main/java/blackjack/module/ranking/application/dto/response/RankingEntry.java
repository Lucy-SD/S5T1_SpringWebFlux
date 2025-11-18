package blackjack.module.ranking.application.dto.response;

public record RankingEntry(
        String playerName,
        int position,
        int totalGames,
        int gamesWon,
        double winPercentage
) {}
