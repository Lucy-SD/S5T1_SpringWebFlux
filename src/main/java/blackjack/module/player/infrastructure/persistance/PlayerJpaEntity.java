package blackjack.module.player.infrastructure.persistance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@Table("player")
public class PlayerJpaEntity {

    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("games_won")
    private Integer gamesWon;

    @Column("games_lost")
    private Integer gamesLost;

    @Column("games_pushed")
    private Integer gamesPushed;
}
