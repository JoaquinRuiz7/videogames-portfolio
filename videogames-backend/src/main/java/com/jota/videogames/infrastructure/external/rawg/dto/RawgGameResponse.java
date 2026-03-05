package com.jota.videogames.infrastructure.external.rawg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record RawgGameResponse(
    Long id,
    String slug,
    String name,
    String released,
    boolean tba,
    @JsonProperty("background_image")
    String backgroundImage,
    Integer rating,
    Integer metacritic,
    @JsonProperty("esrb_rating")
    ESRBRating esrbRating,
    List<GamePlatform> platforms
) {

}
