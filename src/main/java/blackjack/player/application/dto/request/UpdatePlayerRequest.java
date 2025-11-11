package blackjack.player.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdatePlayerRequest(
        @NotBlank(message = "El nombre del jugador es obligatorio.")
        String newName
) {}
