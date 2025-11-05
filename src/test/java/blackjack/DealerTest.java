package blackjack;

import blackjack.gamer.Gamer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DealerTest {

    @Test
    void newDealer_hasEmptyHandAndZeroScore() {
        Gamer dealer = new Dealer();
        assertThat(dealer.getHand()).isEmpty();
        assertThat(dealer.getScore()).isZero();
    }
}
