package blackjack.aahhrefact.module.game.application.service;

import blackjack.aahhrefact.module.deck.application.usecase.CardDrawer;
import blackjack.aahhrefact.module.game.application.usecase.DealersTurn;
import blackjack.aahhrefact.module.game.domain.port.GameRepository;
import blackjack.exception.GameException;
import blackjack.aahhrefact.module.game.application.usecase.FinishGame;
import blackjack.aahhrefact.module.game.application.usecase.Hit;
import blackjack.aahhrefact.module.game.application.usecase.Stand;
import blackjack.aahhrefact.module.game.domain.entity.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PlayerActionService implements Hit, Stand {
    private final GameRepository gameRepository;
    private final CardDrawer cardDrawer;
    private final DealersTurn dealersTurn;
    private final FinishGame finishGame;

    @Override
    public Mono<Game> hit(String gameId) {
        return gameRepository.findById(gameId)
                .flatMap(game -> {
                    if (!game.canPlayerHit()) {
                        return Mono.error(new GameException("No puedes pedir más cartas - ya tienes 21 o más."));
                    }
                    return cardDrawer.drawCard()
                            .doOnNext(card -> game.getPlayerHand().add(card))
                            .doOnNext(card -> game.setPlayerScore(game.scoreCalculator(game.getPlayerHand())))
                            .thenReturn(game);
                })
                .flatMap(game -> finishGame.shouldFinish(game)
                        .flatMap(should -> should
                                ? finishGame.finish(game.getId())
                                : gameRepository.save(game)));
    }

    @Override
    public Mono<Game> stand(String gameId) {
        return gameRepository.findById(gameId)
                .flatMap(dealersTurn::play)
                .flatMap(game -> finishGame.finish(game.getId()));
    }

}