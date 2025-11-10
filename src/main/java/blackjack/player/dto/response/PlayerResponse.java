package blackjack.player.dto.response;

public record PlayerResponse (
        String id,
        String name,
        int gamesWon,
        int gamesLost,
        int gamesPushed,
        int totalGames,
        double winRate
){
}
