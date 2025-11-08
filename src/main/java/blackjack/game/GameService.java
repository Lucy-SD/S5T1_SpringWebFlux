package blackjack.game;

import blackjack.deck.Card;
import blackjack.deck.DeckService;
import blackjack.exception.GameException;
import blackjack.gamer.Dealer;
import blackjack.gamer.Player;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

public class GameService {

    private DeckService deckService;
    private Flux<Card> currentDeck;
    private AtomicInteger usedCards = new AtomicInteger(0);

    public GameService(DeckService deckService) {
        this.deckService = deckService;
    }

    public Mono<GameState> startNewGame(String name) {
        Dealer dealer = new Dealer();
        Player player = new Player(name);

        this.currentDeck = deckService.createShuffledDeck().cache();
        this.usedCards.set(0);

        return this.currentDeck
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
                    this.usedCards.set(4);
                    return new GameState(dealer, player);

                })
                .onErrorResume(throwable -> {
                    return Mono.error(new GameException("Error al iniciar la partida.", throwable));
                });
    }

    private Mono<Card> getNextCard() {
        int nextCardIndex = usedCards.getAndIncrement();
        return this.currentDeck
                .elementAt(nextCardIndex)
                .onErrorResume(throwable ->
                        Mono.error(new GameException("No hay más cartas en el mazo.", throwable)));
    }

    public Mono<GameState> playerHit(GameState gameState) {
        Player player = gameState.getPlayer();

        if (player.getScore() >= 21) {
            return Mono.error(new GameException("El jugador no puede pedir cartas (puntuación de 21 o más)."));
        }
        return this.getNextCard()
                .map(card -> {
                    player.addCard(card);
                    return gameState;
                });
    }

    public Mono<GameState> playerStand(GameState gameState) {
        Dealer dealer = gameState.getDealer();

        dealer.revealHiddenCard();

        return dealerTurn(gameState);
    }

    private Mono<GameState> dealerTurn(GameState gameState) {
        Dealer dealer = gameState.getDealer();

        if (dealer.getScore() < 17) {
            return dealerHit(gameState)
                    .flatMap(this::dealerTurn);
        }
        return Mono.just(gameState);
    }

    public Mono<GameState> dealerHit(GameState gameState) {
        Dealer dealer = gameState.getDealer();
        if (dealer.getScore() >= 17) {
            return Mono.error(new GameException("El dealer no puede pedir cartas (puntuación de 17 o más)."));
        }
        return getNextCard()
                .map(card -> {
                    dealer.addCard(card, true);
                    return gameState;
                });
    }

    public Mono<GameResult> findOutWinner(GameState gameState) {
        Dealer dealer = gameState.getDealer();
        Player player = gameState.getPlayer();

        if (player.getScore() > dealer.getScore()) {
            return Mono.just(new GameResult(Winner.PLAYER, dealer.getScore(), player.getScore()));
        }

        if (dealer.getScore() > 21) {
            return Mono.just(new GameResult(Winner.PLAYER, dealer.getScore(), player.getScore()));
        }

        if (player.getScore() > 21) {
            return Mono.just(new GameResult(Winner.DEALER, dealer.getScore(), player.getScore()));
        }

        return Mono.just(new GameResult(Winner.PUSH, 0, 0));
    }
}
