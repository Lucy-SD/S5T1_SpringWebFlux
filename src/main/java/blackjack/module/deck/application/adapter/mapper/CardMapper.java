package blackjack.module.deck.application.adapter.mapper;

import blackjack.module.deck.application.dto.response.CardResponse;
import blackjack.module.deck.domain.entity.Card;

public class CardMapper {
    public static CardResponse toResponse(Card card) {
        return new CardResponse(card.value());
    }
}
