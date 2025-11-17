package blackjack.module.game.application.service;

import blackjack.module.deck.domain.entity.Card;
import blackjack.module.game.application.usecase.DealersTurn;
import blackjack.module.game.domain.port.GameRepository;

import blackjack.shared.exception.GameException;
import blackjack.module.game.application.usecase.FinishGame;
import blackjack.module.game.application.usecase.Hit;
import blackjack.module.game.application.usecase.Stand;
import blackjack.module.game.domain.entity.Game;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayActionService implements Hit, Stand {
    private final GameRepository gameRepository;
    private final DealersTurn dealersTurn;
    private final FinishGame finishGame;

    @Override

    public Mono<Game> hit(String gameId) {

     return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("❌ Game no encontrado: {}", gameId);
                    return Mono.error(new GameException("Juego no encontrado"));
                }))
                .flatMap(game -> {
                    log.info("Estado del juego: {}, Puntuación del jugador: {}",
                            game.getStatus(), game.getPlayerScore());

                    if (!game.canPlayerHit()) {
                        log.warn("No se puede realizar un Hit.");
                        return Mono.error(new GameException("No puedes pedir más cartas"));
                    }

                    try {
                        Card card = game.drawCardFromDeck();
                        log.info("Nueva carta: {}", card);

                        game.getPlayerHand().add(card);
                        game.setPlayerScore(game.scoreCalculator(game.getPlayerHand()));
                        log.info("Nuevo puntaje: {}", game.getPlayerScore());

                        return gameRepository.save(game)
                                .flatMap(updatedGame -> finishGame.shouldFinish(updatedGame)
                                        .flatMap(should -> should ?
                                                finishGame.finish(updatedGame.getId()) :
                                                Mono.just(updatedGame)
                                        )
                                );
                    } catch (Exception e) {
                        log.error("Error al intentar realizar el hit: {}", e.getMessage(), e);
                        return Mono.error(new GameException("Error durante el hit: " + e.getMessage()));
                    }
                })
                .doOnError(error -> log.error("💥 Error general en HIT: {}", error.getMessage()));
    }

    @Override
    public Mono<Game> stand(String gameId) {
        return gameRepository.findById(gameId)
                .flatMap(dealersTurn::play)
                .flatMap(gameRepository::save)
                .flatMap(savedGame -> finishGame.finish(savedGame.getId()));
    }
}