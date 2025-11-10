package blackjack.player.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table(name = "player")
public class Player {

    @Id
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
    public int getTotalGames() {
        return this.gamesWon + this.gamesLost + this.gamesPushed;
    }
    public double getWinRate() {
        return getTotalGames() > 0 ? (double) this.gamesWon / getTotalGames() : 0.0;
    }
}
