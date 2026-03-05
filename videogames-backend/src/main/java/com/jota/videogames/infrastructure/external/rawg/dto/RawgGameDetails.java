package com.jota.videogames.infrastructure.external.rawg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record RawgGameDetails(
    String description,
    @JsonProperty("metacritic_url")
    String metacriticUrl,
    Integer playtime,
    List<RawgPublisher> publishers
) {

}
