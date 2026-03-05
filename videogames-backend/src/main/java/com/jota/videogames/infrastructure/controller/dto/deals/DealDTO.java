package com.jota.videogames.infrastructure.controller.dto.deals;

import java.math.BigDecimal;

public record DealDTO(
    String dealUrl,
    BigDecimal dealPrice,
    String dealGame,
    StoreResponse store
) {

}
