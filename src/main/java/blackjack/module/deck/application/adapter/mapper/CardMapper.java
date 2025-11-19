package blackjack.module.deck.application.adapter.mapper;

import blackjack.module.deck.application.dto.response.CardResponse;
import blackjack.module.deck.domain.valueObject.Card;
import org.springframework.stereotype.Controller;

@Controller
public class CardMapper {
    public CardResponse toResponse(Card card) {
        return new CardResponse(card.value());
    }
}
