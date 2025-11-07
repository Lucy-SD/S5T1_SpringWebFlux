package blackjack.game;

import blackjack.deck.Card;
import blackjack.deck.DeckService;
import blackjack.exception.GameException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @ParameterizedTest
    @CsvSource({
            "1, 10",
            "10, 9, 2",
            "10, 9, 5"
    })
    void playerCannotHitWhen(int card1, int card2, Integer hitCard) {
        List<Card> mockCards = new ArrayList<>();
        mockCards.add(new Card(card1));
        mockCards.add(new Card(5));
        mockCards.add(new Card(card2));
        mockCards.add(new Card(7));
        if (hitCard != null) {
            mockCards.add(new Card(hitCard));
        }
        when(deckService.createShuffledDeck()).thenReturn(Flux.fromIterable(mockCards));

        GameState gameState = gameService.startNewGame("Pepe").block();

        assertThatExceptionOfType(GameException.class)
                .isThrownBy(() -> gameService.playerHit(gameState).block())
                .withMessageContaining("no puede pedir carta");
    }

}
