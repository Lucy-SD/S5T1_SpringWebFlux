package blackjack.game.dto.request;

import blackjack.game.model.PlayerAction;

public record PlayRequest(
        PlayerAction action
) {}
