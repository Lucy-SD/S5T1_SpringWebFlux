package blackjack.gamer;

import blackjack.deck.Card;

import java.util.List;

public interface Gamer {
    List<Card> getHand();
    int getScore();
    void addCard(Card card);
    boolean hasBlackjack();


}
