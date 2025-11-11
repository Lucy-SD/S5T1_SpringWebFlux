package blackjack.player.application.service;

import blackjack.exception.GameException;
import blackjack.aahhrefact.module.player.domain.entity.Player;
import blackjack.player.infrastructure.persistence.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PlayerFinder {
    private final PlayerRepository playerRepository;

    public Mono<Player> findOrCreate(String playerName) {
        return playerRepository.findByName(playerName)
                .switchIfEmpty(Mono.defer(() -> createPlayer(playerName)));
    }

    public Mono<Player> findById(Long playerId) {
        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.error(new GameException("No se encontró el jugador con ID: " + playerId)));
    }

    private Mono<Player> createPlayer(String name) {
        Player player = Player.builder()
                .name(name)
                .build();
        return playerRepository.save(player);
    }
}
