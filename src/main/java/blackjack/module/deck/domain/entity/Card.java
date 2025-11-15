package blackjack.module.deck.domain.entity;

public record Card(int value) {
    public Card {
        if (value < 1 || value > 10) {
            throw new IllegalArgumentException("Los valores de las cartas deben estar entre 1 y 10.");
        }
    }
}
