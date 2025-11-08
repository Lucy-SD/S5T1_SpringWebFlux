package blackjack.game;

import blackjack.deck.Card;
import blackjack.deck.DeckService;
import blackjack.exception.GameException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private DeckService deckService;

    @InjectMocks
    private GameService gameService;

    @Test
    void startNewGame_setsCorrectGameState() {
        List<Card> mockCards = Arrays.asList(
                new Card(10),
                new Card(7),
                new Card(8),
                new Card(10)
        );

        when(deckService.createShuffledDeck()).thenReturn(Flux.fromIterable(mockCards));

        Mono<GameState> gameStateMono = gameService.startNewGame("Pepe");
        GameState gameState = gameStateMono.block();

        Assertions.assertNotNull(gameState);
        assertThat(gameState.getPlayer().getHand()).hasSize(2);
        assertThat(gameState.getDealer().getHand()).hasSize(2);
        assertThat(gameState.getDealer().getVisibleCards()).hasSize(1);
        assertThat(gameState.getDealer().getHiddenCard()).isNotNull();

    }

    @Test
    void playerCannotHit_whenScoreIs21OrMore() {
        List<Card> mockCards = Arrays.asList(
                new Card(1),
                new Card(2),
                new Card(10),
                new Card(10)
        );
        when(deckService.createShuffledDeck()).thenReturn(Flux.fromIterable(mockCards));
        GameState gameState = gameService.startNewGame("Pepe").block();

        assertThatExceptionOfType(GameException.class)
                .isThrownBy(() -> gameService.playerHit(gameState).block())
                .withMessageContaining("no puede pedir cartas");
    }

    @Test
    void playerCanHit_whenScoreIsUnder21_thenCardIsAddedToHandAndScoreIsUpdated() {
        List<Card> mockCards = Arrays.asList(
                new Card(5),
                new Card(7),
                new Card(5),
                new Card(5),
                new Card(3)
        );

        when(deckService.createShuffledDeck()).thenReturn(Flux.fromIterable(mockCards));
        GameState gameState = gameService.startNewGame("Pepe").block();

        assertThat(gameState.getPlayer().getHand()).hasSize(2);
        assertThat(gameState.getPlayer().getScore()).isEqualTo(10);

        GameState newState = gameService.playerHit(gameState).block();

        assertThat(newState.getPlayer().getHand()).hasSize(3);
        assertThat(newState.getPlayer().getScore()).isEqualTo(13);

        List<Integer> cardValues = newState.getPlayer().getHand()
                .stream()
                .map(Card::value)
                .collect(Collectors.toList());
        assertThat(cardValues).containsExactly(5, 5, 3);
    }

    @Test
    void playerStands_triggersDealerToRevealCardAndPlay() {
        List<Card> mockCards = Arrays.asList(
                new Card(10),
                new Card(6),
                new Card(8),
                new Card(10),
                new Card(1)
        );

        when(deckService.createShuffledDeck()).thenReturn(Flux.fromIterable(mockCards));
        GameState gameState = gameService.startNewGame("Pepe").block();

        GameState finalState = gameService.playerStand(gameState).block();

        assertThat(finalState.getDealer().getHand()).hasSize(3);
        assertThat(finalState.getDealer().getVisibleCards()).hasSize(3);
        assertThat(finalState.getDealer().getHiddenCard()).isNull();
        assertThat(finalState.getDealer().getScore()).isGreaterThanOrEqualTo(17);
    }

    @Test
    void dealerHit_addsCardToHandAndUpdatesScore() {
        List<Card> mockCards = Arrays.asList(
                new Card(10),
                new Card(6),
                new Card(8),
                new Card(10),
                new Card(3)
        );

        when(deckService.createShuffledDeck()).thenReturn(Flux.fromIterable(mockCards));
        GameState gameState = gameService.startNewGame("Pepe").block();

        gameState.getDealer().revealHiddenCard();

        GameState newState = gameService.dealerHit(gameState).block();

        assertThat(newState.getDealer().getHand()).hasSize(3);
        assertThat(newState.getDealer().getVisibleCards()).hasSize(3);
        assertThat(newState.getDealer().getHiddenCard()).isNull();
        assertThat(newState.getDealer().getScore()).isEqualTo(19);
    }

    @Test
    void whenDealerBusts_thenPlayerWins() {
        List<Card> mockCards = Arrays.asList(
                new Card(10),
                new Card(3),
                new Card(8),
                new Card(10),
                new Card(9)
        );

        when(deckService.createShuffledDeck()).thenReturn(Flux.fromIterable(mockCards));
        GameState gameState = gameService.startNewGame("Pepe").block();
        GameState finalState = gameService.playerStand(gameState).block();

        GameResult result = gameService.findOutWinner(finalState).block();

        assertThat(result.getWinner()).isEqualTo(Winner.PLAYER);
        assertThat(result.getPlayerScore()).isEqualTo(18);
        assertThat(result.getDealerScore()).isEqualTo(22);
    }

    @Test
    void whenPlayerStandsWithHigherScore_thenPlayerWins() {
        List<Card> mockCards = Arrays.asList(
                new Card(10),
                new Card(7),
                new Card(9),
                new Card(10)
        );

        when(deckService.createShuffledDeck()).thenReturn(Flux.fromIterable(mockCards));
        GameState gameState = gameService.startNewGame("Pepe").block();
        GameState finalState = gameService.playerStand(gameState).block();

        GameResult result = gameService.findOutWinner(finalState).block();

        assertThat(result.getWinner()).isEqualTo(Winner.PLAYER);
        assertThat(result.getPlayerScore()).isEqualTo(19);
        assertThat(result.getDealerScore()).isEqualTo(17);
    }

    @Test
    void whenPlayerStandAndDealerBusts_thenPlayerWins() {
        List<Card> mockCards = Arrays.asList(
                new Card(10),
                new Card(6),
                new Card(7),
                new Card(7),
                new Card(3),
                new Card(6)

        );

        when(deckService.createShuffledDeck()).thenReturn(Flux.fromIterable(mockCards));
        GameState gameState = gameService.startNewGame("Pepe").block();
        GameState finalState = gameService.playerStand(gameState).block();

        GameResult result = gameService.findOutWinner(finalState).block();

        assertThat(result.getWinner()).isEqualTo(Winner.PLAYER);
        assertThat(result.getPlayerScore()).isEqualTo(17);
        assertThat(result.getDealerScore()).isEqualTo(22);
    }

}
