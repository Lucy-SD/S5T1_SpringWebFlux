package blackjack.module.game.application.adapter.mapper;

import blackjack.module.deck.application.adapter.mapper.CardMapper;
import blackjack.module.game.application.dto.response.GameResponse;
import blackjack.module.game.domain.entity.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GameResponseMapper {

    private final CardMapper cardMapper;

    public GameResponse toResponse(Game game, String playerName) {
        return new GameResponse(
                game.getId(),
                playerName,
                game.getPlayerHand().stream()
                        .map(CardMapper::toResponse)
                        .collect(Collectors.toList()),
                game.getPlayerScore(),
                game.getVisibleCards().stream()
                        .map(CardMapper::toResponse)
                        .collect(Collectors.toList()),
                game.calculateVisibleScore(),
                game.isHasHiddenCard(),
                game.getStatus()
        );
    }
}