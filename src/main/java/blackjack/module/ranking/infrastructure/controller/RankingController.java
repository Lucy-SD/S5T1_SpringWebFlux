package blackjack.module.ranking.infrastructure.controller;

import blackjack.module.ranking.application.dto.response.RankingResponse;
import blackjack.module.ranking.application.usecase.GetPlayersRanking;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ranking")
@RequiredArgsConstructor
@Tag(name = "Ranking", description = "API's para consultar el ranking de los jugadores del BlackJack.")
public class RankingController {
    private final GetPlayersRanking getPlayersRanking;

    @Operation(summary = "Obtener ranking.", description = "Obtiene el ranking de los jugadores.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ranking obtenido exitosamente.")
    })
    @GetMapping
    public Mono<ResponseEntity<RankingResponse>> getRanking(
            @Parameter(description = "Límite de resultados (10 por default).", example = "3")
            @RequestParam(defaultValue = "10") int limit) {
        return getPlayersRanking.getTopPlayers(limit)
                .map(RankingResponse::new)
                .map(ResponseEntity::ok);
    }
}
