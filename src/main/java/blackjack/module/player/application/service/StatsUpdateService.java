package blackjack.module.player.application.service;

import blackjack.module.game.domain.entity.Game;
import blackjack.module.player.domain.entity.Player;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsUpdateService {

    private final StatsService statsService;

    public Mono<Void> updateStatsWhenGameFinished(Game game) {
        if (!game.getStatus().isFinished()) {
            log.debug("El juego con ID: {} no está finalizado.", game.getId());
            return Mono.empty();
        }
        if (game.getResult() == null) {
            log.warn("El juego con ID: {} está finalizado sin resultado.", game.getId());
            return Mono.empty();
        }
        Mono<Player> statsUpdate = switch (game.getResult().winner()) {
            case PLAYER -> {
                log.info("Se ha sumando una victoria al jugador con ID: {}", game.getPlayerId());
                yield  statsService.incrementWins(game.getPlayerId());
            }
            case DEALER -> {
                log.info("Se ha sumando una derrota al jugador con ID: {}", game.getPlayerId());
                yield statsService.incrementLosses(game.getPlayerId());
            }
            case PUSH -> {
                log.info("Se ha sumando un empate al jugador con ID: {}", game.getPlayerId());
                yield statsService.incrementPushes(game.getPlayerId());
            }
        };
        return statsUpdate.then();
    }
}
