package blackjack.game.application.service;

import blackjack.game.application.usecase.FinishGame;
import blackjack.game.domain.Game;
import blackjack.game.domain.GameStatus;
import blackjack.game.domain.Winner;
import blackjack.game.infrastructure.persistence.GameRepository;
import blackjack.player.infrastructure.persistence.PlayerRepository;
import blackjack.aahhrefact.module.deck.domain.service.ScoreCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FinishGameService implements FinishGame {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final ScoreCalculator scoreCalculator;

    @Override
    public Mono<Game> shouldFinish(Game game) {
        if (game.getPlayerScore() >= 21) {
            return finish(game.getId());
        }

        boolean playerHasBlackjack = game.getPlayerHand().size() == 2 && game.getPlayerScore() == 21;
        boolean dealerHasBlackjack = game.getDealerHand().size() == 2 &&
                scoreCalculator.calculate(game.getDealerHand()) == 21;

        if (playerHasBlackjack || dealerHasBlackjack) {
            return finish(game.getId());
        }

        if (!game.isDealerFirstCardHidden() && game.getDealerScore() >= 17) {
            return finish(game.getId());
        }
        return Mono.just(game);
    }

    @Override
    public Mono<Game> finish(String gameId) {
        return gameRepository.findById(gameId)
                .flatMap(game -> {
                    Winner winner = determineWinner(game);
                    return updatePlayerStats(game.getPlayerId(), winner)
                            .then(Mono.just(game));
                })
                .flatMap(game -> {
                    game.setStatus(GameStatus.FINISHED);
                    game.setDealerFirstCardHidden(false);
                    return gameRepository.save(game);
                });
    }

    private Winner determineWinner(Game game) {
        int realDealerScore = scoreCalculator.calculate(game.getDealerHand());

        if (game.getPlayerScore() > 21) return Winner.DEALER;
        if (realDealerScore > 21) return Winner.PLAYER;
        if (game.getPlayerScore() > realDealerScore) return Winner.PLAYER;
        if (realDealerScore > game.getPlayerScore()) return Winner.DEALER;
        return Winner.PUSH;
    }

    private Mono<Void> updatePlayerStats(Long playerId, Winner winner) {
        return playerRepository.findById(playerId)
                .flatMap(player -> {
                    switch (winner) {
                        case PLAYER -> player.increaseGamesWon();
                        case DEALER -> player.increaseGamesLost();
                        case PUSH -> player.increaseGamesPushed();
                    }
                    return playerRepository.save(player);
                })
                .then();
    }
}
