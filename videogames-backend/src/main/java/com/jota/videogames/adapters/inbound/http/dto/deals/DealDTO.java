package com.jota.videogames.adapters.inbound.http.dto.deals;

import java.math.BigDecimal;

public record DealDTO(
        String dealUrl,
        BigDecimal dealPrice,
        String dealGame,
        StoreResponse store
) {

}
