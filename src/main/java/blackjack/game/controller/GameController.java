package blackjack.game.controller;

import blackjack.exception.GameException;
import blackjack.game.dto.response.GameResponse;
import blackjack.game.mapper.GameMapper;
import blackjack.game.mapper.GameResponseMapper;
import blackjack.game.service.ReadGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/game")
public class GameController {
    private final ReadGameService readGameService;
    private final GameMapper gameMapper;
    private final GameResponseMapper gameResponseMapper;

    public GameController(ReadGameService readGameService, GameMapper gameMapper, GameResponseMapper gameResponseMapper) {
        this.readGameService = readGameService;
        this.gameMapper = gameMapper;
        this.gameResponseMapper = gameResponseMapper;
    }

    @GetMapping("/{gameId}")
    public Mono<ResponseEntity<GameResponse>> getGame(@PathVariable String gameId) {
        return readGameService.findGameById(gameId)
                .flatMap(gameEntity -> gameMapper.toGameState(gameEntity)
                        .map(gameState -> gameResponseMapper.toGameResponse(
                                gameState, gameId, gameEntity.getStatus()
                        ))
                        .map(ResponseEntity::ok)
                )
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.notFound().build())
                );
    }
}
