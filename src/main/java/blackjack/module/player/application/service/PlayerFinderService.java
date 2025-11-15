package blackjack.module.player.application.service;

import blackjack.module.player.application.usecase.FindOrCreatePlayer;
import blackjack.module.player.application.usecase.GetPlayer;
import blackjack.module.player.domain.entity.Player;
import blackjack.module.player.domain.port.PlayerRepository;
import blackjack.shared.exception.GameException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PlayerFinderService implements FindOrCreatePlayer, GetPlayer {
    private final PlayerRepository playerRepository;
    private static final Logger log = LoggerFactory.getLogger(PlayerFinderService.class);

    @Override
    public Mono<Player> findPlayerById(Long id) {
        return playerRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameException(("No se encotró el jugador con ID: " + id + "."))));
    }

    @Override
    public Mono<Player> getPlayerByName(String name) {
        return playerRepository.findByName(name)
                .switchIfEmpty(Mono.error(new GameException("No se encontró el jugador con nombre: " + name + ".")));
    }

    @Override
    public Mono<Player> findOrCreatePlayerByName(String name) {
        log.info("🔍 Buscando o creando player: {}", name);

        return playerRepository.existsByName(name)
                .doOnSuccess(exists -> log.info("📊 Player '{}' existe en BD: {}", name, exists))
                .doOnError(error -> log.error("❌ Error verificando existencia de player '{}': {}", name, error.getMessage(), error))
                .flatMap(exists -> {
                    if (exists) {
                        log.info("✅ Player '{}' existe, buscando...", name);
                        return playerRepository.findByName(name)
                                .doOnSuccess(player -> log.info("🎯 Player encontrado: {}", player))
                                .doOnError(error -> log.error("❌ Error buscando player '{}': {}", name, error.getMessage(), error));
                    } else {
                        log.info("🆕 Player '{}' no existe, creando nuevo...", name);
                        return createNewPlayer(name)
                                .doOnSuccess(player -> log.info("✨ Nuevo player creado: {}", player))
                                .doOnError(error -> log.error("❌ Error creando player '{}': {}", name, error.getMessage(), error));
                    }
                });
    }

    @Override
    public Mono<Player> getPlayerById(Long id) {
        return playerRepository.findById(id);
    }

    private Mono<Player> createNewPlayer(String name) {
        log.info("🏗️ Creando nuevo player: {}", name);

        Player newPlayer = Player.builder()
                .name(name)
                .gamesWon(0)
                .gamesLost(0)
                .gamesPushed(0)
                .build();

        return playerRepository.save(newPlayer)
                .doOnSuccess(savedPlayer -> log.info("💾 Player guardado en BD: {}", savedPlayer))
                .doOnError(error -> log.error("💥 Error guardando player en BD: {}", error.getMessage(), error));
    }
}
