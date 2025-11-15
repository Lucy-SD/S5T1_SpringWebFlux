package blackjack.module.game.application.service;

import blackjack.module.game.application.usecase.SaveGame;
import blackjack.module.game.domain.entity.Game;
import blackjack.module.game.domain.port.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveGameService implements SaveGame {

    private final GameRepository gameRepository;

    public Mono<Game> saveGame(Game game) {
        return gameRepository.save(game)
                .doOnSuccess(saved -> log.info("El juego se guardo correctamente."))
                .doOnError(error -> log.error("Error al guardar el juego: {}", error.getMessage()));
    }
}
