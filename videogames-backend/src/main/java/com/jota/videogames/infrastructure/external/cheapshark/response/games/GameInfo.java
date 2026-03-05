package com.jota.videogames.infrastructure.external.cheapshark.response.games;

import java.math.BigDecimal;

public record GameInfo(
    String storeID,
    String gameID,
    String name,
    String steamAppID,
    BigDecimal salePrice,
    BigDecimal retailPrice,
    String steamRatingText,
    String steamRatingPercent,
    String steamRatingCount,
    String metacriticScore,
    String metacriticLink,
    Long releaseDate,
    String publisher,
    String steamworks,
    String thumb
) {

}
