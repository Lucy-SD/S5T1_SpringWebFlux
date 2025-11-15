package blackjack.module.player.application.dto.response;

public record PlayerResponse (
        Long id,
        String name,
        int gamesWon,
        int gamesLost,
        int gamesPushed,
        int totalGames,
        double winRate
){
}
