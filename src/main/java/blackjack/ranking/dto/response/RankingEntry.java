package blackjack.ranking.dto.response;

public record RankingEntry(
        String playerName,
        int position,
        int totalGames,
        int gamesWon,
        double winRate
) {}
