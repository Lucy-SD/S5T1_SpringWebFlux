package blackjack.game.application.service;

import blackjack.deck.application.service.CardDrawer;
import blackjack.exception.GameException;
import blackjack.game.application.usecase.DealersTurn;
import blackjack.game.application.usecase.FinishGame;
import blackjack.game.application.usecase.Hit;
import blackjack.game.application.usecase.Stand;
import blackjack.game.domain.Game;
import blackjack.game.infrastructure.persistence.GameRepository;
import blackjack.shared.ScoreCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PlayerActionService implements Hit, Stand {
    private final GameRepository gameRepository;
    private final CardDrawer cardDrawer;
    private final ScoreCalculator scoreCalculator;
    private final DealersTurn dealersTurn;
    private final FinishGame finishGame;

    @Override
    public Mono<Game> hit(String gameId) {
        return gameRepository.findById(gameId)
                .flatMap(this::validateCanHit)
                .flatMap(game -> cardDrawer.drawCard()
                        .map(card -> {
                            game.getPlayerHand().add(card);
                            game.setPlayerScore(scoreCalculator.calculate(game.getPlayerHand()));
                            return game;
                        }))
                .flatMap(gameRepository::save)
                .flatMap(this::handlePossibleCompletion);
    }

    @Override
    public Mono<Game> stand(String gameId) {
        return gameRepository.findById(gameId)
                .flatMap(dealersTurn::play);
    }

    private Mono<Game> validateCanHit(Game game) {
        if (game.getPlayerScore() >= 21) {
            return Mono.error(new GameException("No puedes pedir más cartas - ya tienes 21 o más."));
        }
        return Mono.just(game);
    }

    private Mono<Game> handlePossibleCompletion(Game game) {
        return finishGame.shouldFinish(game);
    }
}
