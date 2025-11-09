package blackjack.gamer.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "player")
public class PlayerEntity {

    @Id
    private Long id;

    private String name;
    private Integer gamesWon;
    private Integer gamesLost;
    private Integer gamesPushed;

    public PlayerEntity() {}

    public PlayerEntity(String name) {
        this.name = name;
        this.gamesWon = 0;
        this.gamesLost = 0;
        this.gamesPushed = 0;
    }



}
