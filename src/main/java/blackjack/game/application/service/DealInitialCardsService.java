package blackjack.game.application.service;

import blackjack.deck.application.service.CardDrawer;
import blackjack.game.application.usecase.DealInitialCards;
import blackjack.game.domain.Game;
import blackjack.shared.ScoreCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DealInitialCardsService implements DealInitialCards {
    private final CardDrawer cardDrawer;
    private final ScoreCalculator scoreCalculator;

    @Override
    public Mono<Game> deal(Game game) {
        return cardDrawer.drawCard()
                .flatMap(card1 -> {
                    game.getPlayerHand().add(card1);
                    return cardDrawer.drawCard();
                })
                .flatMap(card2 -> {
                    game.getDealerHand().add(card2);
                    game.setDealerScore(card2.value());
                    return cardDrawer.drawCard();
                })
                .flatMap(card3 -> {
                    game.getPlayerHand().add(card3);
                    return cardDrawer.drawCard();
                })
                .map(card4 -> {
                    game.getDealerHand().add(card4);
                    game.setPlayerScore(scoreCalculator.calculate(game.getPlayerHand()));
                    return game;
                });
    }
}
