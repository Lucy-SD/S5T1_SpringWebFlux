package blackjack.module.game.application.dto.request;

import blackjack.module.game.domain.valueObject.PlayerAction;

public record PlayRequest(
        PlayerAction action
) {}
