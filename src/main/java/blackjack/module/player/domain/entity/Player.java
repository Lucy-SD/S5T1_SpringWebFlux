package blackjack.module.player.domain.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Player {

    private Long id;
    private String name;
    @Builder.Default
    private Integer gamesWon = 0;
    @Builder.Default
    private Integer gamesLost = 0;
    @Builder.Default
    private Integer gamesPushed = 0;

    public void increaseGamesWon() {
        this.gamesWon++;
    }
    public void increaseGamesLost() {
        this.gamesLost++;
    }
    public void increaseGamesPushed() {
        this.gamesPushed++;
    }
    public int calculateTotalGames() {
        return this.gamesWon + this.gamesLost + this.gamesPushed;
    }
    public double calculateWinRate() {
        return calculateTotalGames() > 0 ? (double) this.gamesWon / calculateTotalGames() : 0.0;
    }
}
