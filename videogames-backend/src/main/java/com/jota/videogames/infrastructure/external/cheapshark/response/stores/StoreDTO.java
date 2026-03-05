package com.jota.videogames.infrastructure.external.cheapshark.response.stores;

import com.jota.videogames.infrastructure.controller.dto.deals.StoreImages;

public record StoreDTO(
    String storeID,
    String storeName,
    StoreImages images
) {

}
