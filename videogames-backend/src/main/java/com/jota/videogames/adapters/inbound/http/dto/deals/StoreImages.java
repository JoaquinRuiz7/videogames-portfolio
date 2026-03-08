package com.jota.videogames.adapters.inbound.http.dto.deals;

public record StoreImages(
        String logo,
        String banner,
        String icon
) {

    private static final String BASE_URL = "https://www.cheapshark.com";

    public StoreImages withUrl(
            final String logo,
            final String banner,
            final String icon
    ) {
        return new StoreImages(
                BASE_URL + logo,
                BASE_URL + banner,
                BASE_URL + icon
        );
    }
}
