package blackjack.module.player.application.service;

import blackjack.module.player.application.usecase.FindOrCreatePlayer;
import blackjack.module.player.domain.entity.Player;
import blackjack.module.player.domain.PlayerRepository;
import blackjack.shared.exception.GameException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class FindOrCreatePlayerService implements FindOrCreatePlayer {
    private final PlayerRepository playerRepository;

    @Override
    public Mono<Player> findPlayerById(Long id) {
        return playerRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameException(("No se encotró el jugador con ID: " + id + "."))));
    }

    @Override
    @Transactional
    public Mono<Player> findOrCreatePlayerByName(String name) {
        log.info("Buscando o creando jugador: {}", name);

        return playerRepository.findByName(name)
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("Jugador no encontrado. Se ha creado un nuevo jugador: '{}'.", name);
                    return createNewPlayer(name);
                }))
                .doOnError(error -> log.error("No se pudo crear el nuevo jugador '{}': {}", name, error.getMessage()));
    }

    private Mono<Player> createNewPlayer(String name) {
        Player newPlayer = Player.builder()
                .name(name)
                .gamesWon(0)
                .gamesLost(0)
                .gamesPushed(0)
                .build();
        return playerRepository.save(newPlayer);

    }
}
