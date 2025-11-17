package blackjack.module.player.application.service;

import blackjack.module.player.application.usecase.UpdatePlayerName;
import blackjack.module.player.domain.entity.Player;
import blackjack.module.player.domain.port.PlayerRepository;
import blackjack.shared.exception.GameException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NameUpdateService implements UpdatePlayerName {

    private final PlayerRepository playerRepository;

    @Override
    @Transactional
    public Mono<Player> updateName(Long id, String name) {
        return playerRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameException("No se encontró el jugador con ID: " + id + ".")))
                .flatMap(player -> {
                    player.setName(name);
                    return playerRepository.save(player);
                });
    }
}

