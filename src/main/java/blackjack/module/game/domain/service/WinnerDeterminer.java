package blackjack.module.game.domain.service;

import blackjack.module.game.domain.entity.Game;
import blackjack.module.game.domain.valueObject.Winner;

public class WinnerDeterminer {

    public static Winner determine(Game game) {
        int playerScore = game.getPlayerScore();
        int dealerScore = game.getDealerScore();

        if (playerScore > 21) return Winner.DEALER;
        if (dealerScore > 21) return Winner.PLAYER;
        if (playerScore > dealerScore) return Winner.PLAYER;
        if (dealerScore > playerScore) return Winner.DEALER;
        return Winner.PUSH;
    }
}
