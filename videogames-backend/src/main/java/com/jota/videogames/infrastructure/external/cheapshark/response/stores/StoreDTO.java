package com.jota.videogames.infrastructure.external.cheapshark.response.stores;

import com.jota.videogames.adapters.inbound.http.dto.deals.StoreImages;

public record StoreDTO(
        String storeID,
        String storeName,
        StoreImages images
) {

}
