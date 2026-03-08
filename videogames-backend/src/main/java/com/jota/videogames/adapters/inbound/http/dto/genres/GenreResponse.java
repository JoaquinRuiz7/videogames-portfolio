package com.jota.videogames.adapters.inbound.http.dto.genres;

public record GenreResponse(
        String genreName,
        Integer genreId,
        String genreBackgroundImage
) {

}
