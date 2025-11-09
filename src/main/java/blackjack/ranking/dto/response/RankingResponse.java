package blackjack.ranking.dto.response;

import java.util.List;

public record RankingResponse (
      List<RankingEntry> ranking
){}
