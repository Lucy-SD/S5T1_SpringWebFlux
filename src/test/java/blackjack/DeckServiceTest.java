package blackjack;

import blackjack.deck.Card;
import blackjack.deck.DeckService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
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





}
