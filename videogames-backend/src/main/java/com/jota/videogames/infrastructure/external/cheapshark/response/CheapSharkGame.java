package com.jota.videogames.infrastructure.external.cheapshark.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record CheapSharkGame(
    @JsonProperty("gameID")
    Long gameId,
    @JsonProperty("steamAppID")
    Long steamAppId,
    @JsonProperty("cheapest")
    BigDecimal cheapestPrice,
    @JsonProperty("cheapestDealID")
    String cheapestDealId,
    @JsonProperty("external")
    String externalName,
    String internalName,
    @JsonProperty("thumb")
    String thumbnail
) {

}

