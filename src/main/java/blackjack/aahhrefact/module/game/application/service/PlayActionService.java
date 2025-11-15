package blackjack.aahhrefact.module.game.application.service;

import blackjack.aahhrefact.module.deck.domain.entity.Card;
import blackjack.aahhrefact.module.game.application.usecase.DealersTurn;
import blackjack.aahhrefact.module.game.domain.port.GameRepository;
import blackjack.aahhrefact.module.player.application.service.PlayerFinderService;
import blackjack.exception.GameException;
import blackjack.aahhrefact.module.game.application.usecase.FinishGame;
import blackjack.aahhrefact.module.game.application.usecase.Hit;
import blackjack.aahhrefact.module.game.application.usecase.Stand;
import blackjack.aahhrefact.module.game.domain.entity.Game;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PlayActionService implements Hit, Stand {
    private final GameRepository gameRepository;
    private final DealersTurn dealersTurn;
    private final FinishGame finishGame;
    private Logger log = LoggerFactory.getLogger(PlayerFinderService.class);

    @Override

    public Mono<Game> hit(String gameId) {

        log.info("🎯 Iniciando HIT para gameId: {}", gameId);

        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("❌ Game no encontrado: {}", gameId);
                    return Mono.error(new GameException("Juego no encontrado"));
                }))
                .flatMap(game -> {
                    log.info("🃏 Game encontrado - Estado: {}, PlayerScore: {}",
                            game.getStatus(), game.getPlayerScore());

                    if (!game.canPlayerHit()) {
                        log.warn("⚠️ No se puede HIT - Estado inválido");
                        return Mono.error(new GameException("No puedes pedir más cartas"));
                    }

                    try {
                        Card card = game.drawCardFromDeck();
                        log.info("🎴 Carta dibujada: {}", card);

                        game.getPlayerHand().add(card);
                        game.setPlayerScore(game.scoreCalculator(game.getPlayerHand()));
                        log.info("📊 Nuevo score: {}", game.getPlayerScore());

                        return finishGame.shouldFinish(game)
                                .flatMap(should -> should ?
                                        finishGame.finish(game.getId()) :
                                        gameRepository.save(game)
                                );
                    } catch (Exception e) {
                        log.error("💥 Error en HIT: {}", e.getMessage(), e);
                        return Mono.error(new GameException("Error durante el hit: " + e.getMessage()));
                    }
                })
                .doOnError(error -> log.error("💥 Error general en HIT: {}", error.getMessage()));
    }

    @Override
    public Mono<Game> stand(String gameId) {
        return gameRepository.findById(gameId)
                .flatMap(dealersTurn::play)
                .flatMap(gameRepository::save);
    }
}