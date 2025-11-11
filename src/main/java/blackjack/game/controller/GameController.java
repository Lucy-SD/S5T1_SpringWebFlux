package blackjack.game.controller;

import blackjack.exception.GameException;
import blackjack.game.application.usecase.CreateGame;
import blackjack.game.application.usecase.Hit;
import blackjack.game.application.usecase.Stand;
import blackjack.game.domain.Game;
import blackjack.game.domain.Winner;
import blackjack.game.dto.request.CreateGameRequest;
import blackjack.game.dto.request.PlayRequest;
import blackjack.game.dto.response.GameResponse;
import blackjack.game.dto.response.PlayResponse;
import blackjack.game.domain.GameResult;
import blackjack.game.infrastructure.persistence.GameRepository;
import blackjack.game.infrastructure.web.mapper.GameResponseMapper;
import blackjack.game.domain.GameStatus;
import blackjack.aahhrefact.module.deck.domain.service.ScoreCalculator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {
    private final GameRepository gameRepository;
    private final CreateGame createGame;
    private final Hit hit;
    private final Stand stand;
    private final ScoreCalculator scoreCalculator;
    private final GameResponseMapper responseMapper;


    @PostMapping("/new")
    public Mono<ResponseEntity<GameResponse>> createGame(@Valid @RequestBody CreateGameRequest request) {
        return createGame.create(request.playerName())
                .map(game -> createGameResponse(game))
                .map(gameResponse -> ResponseEntity.status(HttpStatus.CREATED).body(gameResponse))
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.badRequest().build())
                );
    }

    @GetMapping("/{gameId}")
    public Mono<ResponseEntity<GameResponse>> getGame(@PathVariable String gameId) {
        return gameRepository.findById(gameId)
                .map(this::createGameResponse)
                .map(ResponseEntity::ok)
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.notFound().build())
                );
    }

    @PostMapping("/{gameId}/play")
    public Mono<ResponseEntity<PlayResponse>> playGame(
            @PathVariable String gameId,
            @Valid @RequestBody PlayRequest request) {

        Mono<Game> actionResult = switch (request.action()) {
            case HIT -> hit.hit(gameId);
            case STAND -> stand.stand(gameId);
        };

        return actionResult
                .flatMap(this::processGameResult)
                .map(ResponseEntity::ok)
                .onErrorResume(GameException.class, e ->
                        Mono.just(ResponseEntity.badRequest().build())
                );
    }

    @DeleteMapping("/{gameId}")
    public Mono<ResponseEntity<Void>> deleteGame(@PathVariable String gameId) {
        return gameRepository.deleteById(gameId)
                .thenReturn(ResponseEntity.noContent().<Void>build())
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }

    private Mono<PlayResponse> processGameResult(Game game) {
        GameResponse gameResponse = createGameResponse(game);

        if (game.getStatus() == GameStatus.FINISHED) {
            GameResult gameResult = determineWinner(game);
            return Mono.just(new PlayResponse(
                    GameStatus.FINISHED,
                    gameResponse,
                    gameResult
            ));
        } else {
            return Mono.just(new PlayResponse(
                    GameStatus.ACTIVE,
                    gameResponse,
                    null
            ));
        }
    }

    private GameResult determineWinner(Game game) {
        int realDealerScore = scoreCalculator.calculate(game.getDealerHand());
        int playerScore = game.getPlayerScore();

        if (playerScore > 21) return new GameResult(Winner.DEALER, realDealerScore, playerScore);
        if (realDealerScore > 21) return new GameResult(Winner.PLAYER, realDealerScore, playerScore);
        if (playerScore > realDealerScore) return new GameResult(Winner.PLAYER, realDealerScore, playerScore);
        if (realDealerScore > playerScore) return new GameResult(Winner.DEALER, realDealerScore, playerScore);
        return new GameResult(Winner.PUSH, realDealerScore, playerScore);
    }

    private GameResponse createGameResponse(Game game) {
        return responseMapper.toGameResponse(game);
    }
}