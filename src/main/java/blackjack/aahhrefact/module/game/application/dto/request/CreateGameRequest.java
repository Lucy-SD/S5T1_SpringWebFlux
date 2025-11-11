package blackjack.aahhrefact.module.game.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateGameRequest(
        @NotBlank(message = "El nombre del jugador es obligatorio.")
        String playerName
) {}
