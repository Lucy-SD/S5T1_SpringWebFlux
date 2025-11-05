package blackjack;

import blackjack.gamer.Gamer;
import blackjack.gamer.Player;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerTest {

    @Test
    void newPlayer_hasEmptyHandAndZeroScore() {
        Gamer player = new Player("Pepe");
        assertThat(player.getHand()).isEmpty();
        assertThat(player.getScore()).isZero();
    }

}
