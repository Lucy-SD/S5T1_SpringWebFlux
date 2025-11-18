package blackjack.module.player.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    public double calculateWinPercentage() {
        return calculateTotalGames() > 0 ?
                Math.round((double)  this.gamesWon * 100 / calculateTotalGames()): 0.0;
    }
}
