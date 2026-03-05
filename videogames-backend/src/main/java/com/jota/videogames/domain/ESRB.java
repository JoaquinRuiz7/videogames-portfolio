package com.jota.videogames.domain;

import java.util.Arrays;

public enum ESRB {
    M("Mature"),
    T("Teen"),
    E10("Everyone 10+"),
    E("Everyone"),
    AO("Adults only"),
    RP("Rating Pending");

    private String rating;

    ESRB(final String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    public static ESRB fromName(String name) {
        return Arrays.stream(values())
            .filter(esrb -> esrb.rating.equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown ESRB enum: " + name));
    }
}
