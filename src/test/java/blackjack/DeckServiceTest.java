package blackjack;

import blackjack.deck.Card;
import blackjack.deck.DeckService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DeckServiceTest {

    @InjectMocks
    private DeckService deckService;

    @Test
    void createDeck_shouldGenerateA52CardsDeck() {
        List<Card> deck = deckService.createDeck();

        assertThat(deck).hasSize(52);
    }

    @Test
    void shuffleDeck_shouldReturnMixedDeck() {
        List<Card> deck = deckService.createDeck();

        List<Card> deck1 = new ArrayList<>(deck);
        List<Card> deck2 = new ArrayList<>(deck);

        List<Card> shuffled1 = deckService.shuffleDeck(deck1);
        List<Card> shuffled2 = deckService.shuffleDeck(deck2);

        assertThat(shuffled1).isNotEqualTo(shuffled2);
    }



}
