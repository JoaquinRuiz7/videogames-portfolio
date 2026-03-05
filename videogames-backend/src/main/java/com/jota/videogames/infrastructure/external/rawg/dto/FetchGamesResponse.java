package com.jota.videogames.infrastructure.external.rawg.dto;

import java.util.List;

public record FetchGamesResponse(
    String next,
    String previous,
    List<RawgGameResponse> results
) {

}
