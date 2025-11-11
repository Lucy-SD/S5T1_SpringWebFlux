package blackjack.aahhrefact.module.game.application.dto.request;

import blackjack.aahhrefact.module.game.domain.valueObject.PlayerAction;

public record PlayRequest(
        PlayerAction action
) {}
