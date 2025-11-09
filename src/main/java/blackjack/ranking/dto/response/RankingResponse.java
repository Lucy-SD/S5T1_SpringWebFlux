package blackjack.ranking.dto.response;

public record RankingResponse (
        String playerName,
        int gamesWon,
        int gamesLost,
        int gamesPushed,
        int totalGames,
        double winRate
){}
