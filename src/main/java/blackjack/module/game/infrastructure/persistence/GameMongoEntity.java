package blackjack.module.game.infrastructure.persistence;

import blackjack.module.deck.domain.entity.Card;
import blackjack.module.game.domain.valueObject.GameResult;
import blackjack.module.game.domain.valueObject.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "game")
public class GameMongoEntity {

    @Id
    private String id;
    private List<Card> remainingCards;
    private Long playerId;
    private List<Card> playerHand;
    private List<Card> dealerHand;
    private Integer playerScore;
    private Integer dealerScore;
    private Boolean hasHiddenCard;
    private GameStatus status;
    private GameResult result;
    private Instant createdAt;
}
