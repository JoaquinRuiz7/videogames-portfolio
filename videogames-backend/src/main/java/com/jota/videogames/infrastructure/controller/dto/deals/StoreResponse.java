package com.jota.videogames.infrastructure.controller.dto.deals;

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
