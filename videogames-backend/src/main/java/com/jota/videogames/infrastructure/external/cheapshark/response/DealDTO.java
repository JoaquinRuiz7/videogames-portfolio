package com.jota.videogames.infrastructure.external.cheapshark.response;

import java.math.BigDecimal;

public record DealDTO(
    Long storeID,
    String dealID,
    BigDecimal price,
    BigDecimal retailPrice
) {

}
