package blackjack.aahhrefact.module.game.infrastructure.persistence;

import blackjack.aahhrefact.module.deck.domain.entity.Card;
import blackjack.aahhrefact.module.game.domain.valueObject.GameResult;
import blackjack.aahhrefact.module.game.domain.valueObject.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Document(collection = "game")
public class GameMongoEntity {

    @Id
    private String id;

    private Long playerId;
    private List<Card> playerHand;
    private List<Card> dealerHand;
    private Integer playerScore;
    private Integer dealerScore;
    private Boolean firstCardHidden;
    private GameStatus status;
    private GameResult result;
    private Instant createdAt;
}
