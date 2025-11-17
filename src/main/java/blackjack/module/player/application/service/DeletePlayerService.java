package blackjack.module.player.application.service;

import blackjack.module.player.application.usecase.DeletePlayer;
import blackjack.module.player.domain.port.PlayerRepository;
import blackjack.shared.exception.GameException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DeletePlayerService implements DeletePlayer {

    private final PlayerRepository playerRepository;

    @Override
    public Mono<Void> delete(Long id) {
        return playerRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameException("No existe jugador con ID: " + id + ".")))
                .flatMap(playerRepository::delete);
    }
}
