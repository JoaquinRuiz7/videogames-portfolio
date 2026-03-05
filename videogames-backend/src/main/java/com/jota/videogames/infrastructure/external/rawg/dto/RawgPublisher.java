package com.jota.videogames.infrastructure.external.rawg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RawgPublisher(
    Long id,
    String name,
    @JsonProperty("image_background")
    String imageBackground
) {

}
