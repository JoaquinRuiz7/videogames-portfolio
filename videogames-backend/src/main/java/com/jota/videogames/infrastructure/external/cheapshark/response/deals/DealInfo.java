package com.jota.videogames.infrastructure.external.cheapshark.response.deals;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record DealInfo(
    @JsonProperty("storeID")
    Long storeId,
    @JsonProperty("gameID")
    Long gameId,
    String name,
    BigDecimal salePrice
) {

}
