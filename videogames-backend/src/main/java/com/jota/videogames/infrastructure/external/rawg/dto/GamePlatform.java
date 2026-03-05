package com.jota.videogames.infrastructure.external.rawg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GamePlatform(
    Platform platform,
    @JsonProperty("released_at")
    String releasedAt
) {

}
