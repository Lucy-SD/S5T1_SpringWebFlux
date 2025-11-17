package blackjack.module.player.application.service;

import blackjack.module.player.application.usecase.FindOrCreatePlayer;
import blackjack.module.player.domain.entity.Player;
import blackjack.module.player.domain.port.PlayerRepository;
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

        return playerRepository.existsByName(name)
                .flatMap(exists -> {
                    if (exists) {
                        log.info("Jugador encontrado: '{}'.", name);
                        return playerRepository.findByName(name);
                    } else {
                        log.info("Creando nuevo jugador: '{}'.", name);
                        return createNewPlayer(name);
                    }
                })
                .doOnError(error -> log.error("No se pudo crear el nuevo jugador '{}': {}", name, error.getMessage()));
    }

    private Mono<Player> createNewPlayer(String name) {
        Player newPlayer = Player.builder()
                .name(name)
                .gamesWon(0)
                .gamesLost(0)
                .gamesPushed(0)
                .build();
        log.info("Se ha creado el jugador: {}", name);

        return playerRepository.save(newPlayer)
                .doOnSuccess(savedPlayer -> log.info("Se guardó correctamente al jugador: '{}'.", savedPlayer))
                .doOnError(error -> log.error("Error al guardar al jugador: {}", error.getMessage()));
    }
}
