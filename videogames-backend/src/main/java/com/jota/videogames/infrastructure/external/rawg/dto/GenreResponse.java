package com.jota.videogames.infrastructure.external.rawg.dto;

import java.util.List;

public record GenreResponse(
    List<RawgGenre> results
) {

}
