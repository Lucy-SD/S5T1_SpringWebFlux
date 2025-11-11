package blackjack.player.application.dto.response;

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
