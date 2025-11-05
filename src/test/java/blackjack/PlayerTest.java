package blackjack;

import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    void newPlayer_hasEmptyHandAndZeroScore() {
        Player player = new Player();
        assertThat(player.getHand()).isEmpty();
        assertThat(player.getScore()).isZero();
    }

}
