package blackjack.module.ranking.application.dto.response;

import java.util.List;

public record RankingResponse (
      List<RankingEntry> ranking
){}
