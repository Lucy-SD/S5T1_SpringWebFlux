package blackjack.aahhrefact.module.deck.application.adapter.mapper;

import blackjack.aahhrefact.module.deck.application.dto.response.CardResponse;
import blackjack.aahhrefact.module.deck.domain.entity.Card;

public class CardMapper {
    public static CardResponse toResponse(Card card) {
        return new CardResponse(card.value());
    }
}
