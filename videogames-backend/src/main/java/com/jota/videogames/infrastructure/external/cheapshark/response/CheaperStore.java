package com.jota.videogames.infrastructure.external.cheapshark.response;

import java.math.BigDecimal;

public record CheaperStore(
    String dealId,
    Integer storeId,
    BigDecimal salePrice,
    BigDecimal retailPrice
) {

}
