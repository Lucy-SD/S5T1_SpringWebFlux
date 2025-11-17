package blackjack.module.deck.domain.entity;

import lombok.Getter;

import java.util.List;

@Getter
public class Deck {
    private final List<Card> cards;

    public Deck(List<Card> cards) {
        this.cards = cards;
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("El mazo está vacio.");
        }
        return cards.removeFirst();
    }

}
