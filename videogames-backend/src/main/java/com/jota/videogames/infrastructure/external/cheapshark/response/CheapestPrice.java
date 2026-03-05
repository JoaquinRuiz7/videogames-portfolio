package com.jota.videogames.infrastructure.external.cheapshark.response;

import java.math.BigDecimal;

public record CheapestPrice(
    BigDecimal price,
    BigDecimal date
) {

}
