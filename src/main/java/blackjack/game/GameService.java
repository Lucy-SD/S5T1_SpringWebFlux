package blackjack.game;

import blackjack.deck.DeckService;
import blackjack.exception.GameException;
import blackjack.gamer.Dealer;
import blackjack.gamer.Player;
import reactor.core.publisher.Mono;

public class GameService {

    private DeckService deckService;

    public GameService(DeckService deckService) {
        this.deckService = deckService;
    }

    public Mono<GameState> startNewGame(String name) {
        Dealer dealer = new Dealer();
        Player player = new Player(name);

        return deckService.createShuffledDeck()
                .take(4)
                .collectList()
                .map(cards -> {
                    if (cards.size() < 4) {
                        throw new IllegalArgumentException("No hay suficientes cartas.");
                    }
                    player.addCard(cards.get(0));
                    dealer.addCard(cards.get(1), false);
                    player.addCard(cards.get(2));
                    dealer.addCard(cards.get(3), true);
                    return new GameState(dealer, player);
                })
                .onErrorResume(throwable -> {
                    return Mono.error(new GameException("Error al iniciar la partida.", throwable));
                });
    }
}
