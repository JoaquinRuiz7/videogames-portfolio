package com.jota.videogames.adapters.inbound.http.dto.genres;

import java.util.List;

public record GenresResponse(
        List<GenreResponse> genres
) {

}
