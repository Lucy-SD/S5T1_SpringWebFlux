package blackjack.game.model;

import blackjack.player.model.Dealer;
import blackjack.player.domain.Player;

public class GameState {

    private Dealer dealer;
    private Player player;

    public GameState(Dealer dealer, Player player) {
        this.dealer = dealer;
        this.player = player;
    }
    public Dealer getDealer() { return this.dealer; }
    public Player getPlayer() { return this.player; }

}
