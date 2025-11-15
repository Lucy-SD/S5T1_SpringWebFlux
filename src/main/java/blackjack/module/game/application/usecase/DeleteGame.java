package blackjack.module.game.application.usecase;

import reactor.core.publisher.Mono;

public interface DeleteGame {
    Mono<Void> delete(String id);
}
