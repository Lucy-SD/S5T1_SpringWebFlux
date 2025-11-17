package blackjack.module.game.infrastructure.persistence;

import blackjack.module.deck.domain.entity.Card;
import blackjack.module.game.domain.valueObject.GameResult;
import blackjack.module.game.domain.valueObject.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "game")
@CompoundIndex(def = "{'playerId': 1, 'createdAt': -1}")
public class GameMongoEntity {

    @Id
    private String id;
    @Field("remainingCards")
    private List<Card> remainingCards;
    @Field("playerId")
    private Long playerId;
    @Field("playerHand")
    private List<Card> playerHand;
    @Field("dealerHand")
    private List<Card> dealerHand;
    @Field("playerScore")
    private Integer playerScore;
    @Field("dealerScore")
    private Integer dealerScore;
    @Field("hasHiddenCard")
    private Boolean hasHiddenCard;
    @Field("status")
    private GameStatus status;
    @Field("result")
    private GameResult result;
    @Field("createdAt")
    private Instant createdAt;
}
