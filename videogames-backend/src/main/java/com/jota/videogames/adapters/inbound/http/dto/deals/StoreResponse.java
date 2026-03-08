package com.jota.videogames.adapters.inbound.http.dto.deals;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StoreResponse(
        String name,
        String logo
) {

    @JsonProperty("logo")
    public String logoUrl() {
        return "https://www.cheapshark.com" + logo;
    }

}
