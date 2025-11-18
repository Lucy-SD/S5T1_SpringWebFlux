package blackjack.module.ranking.application.dto.response;

public record RankingEntry(
        String playerName,
        int position,
        int gamesWon,
        int totalGames,
        double winPercentage
) {}
