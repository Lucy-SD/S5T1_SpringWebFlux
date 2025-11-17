package blackjack.module.player.application.usecase;

import reactor.core.publisher.Mono;

public interface DeletePlayer {
    Mono<Void> delete(Long id);
}
