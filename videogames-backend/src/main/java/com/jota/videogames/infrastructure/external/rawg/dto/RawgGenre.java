package com.jota.videogames.infrastructure.external.rawg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RawgGenre(
    Integer id,
    String name,
    @JsonProperty("image_background")
    String imageBackground
) {

}
