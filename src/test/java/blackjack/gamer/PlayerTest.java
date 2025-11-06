package blackjack.gamer;

import blackjack.deck.Card;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerTest {

    @Test
    void newPlayer_hasEmptyHandAndZeroScore() {
        Gamer player = new Player("Pepe");
        assertThat(player.getHand()).isEmpty();
        assertThat(player.getScore()).isZero();
    }

    @Test
    void playerAddsCard_shouldAddCardToHand() {
        Gamer player = new Player("Pepe");
        player.addCard(new Card(7));
        assertThat(player.getHand()).hasSize(1);
        assertThat(player.getHand().get(0).value()).isEqualTo(7);
    }

    @Test
    void playerAddsCard_shouldUpdateScore() {
        Gamer player = new Player("Pepe");
        player.addCard(new Card(7));
        player.addCard(new Card(8));
        assertThat(player.getScore()).isEqualTo(15);
    }

}
