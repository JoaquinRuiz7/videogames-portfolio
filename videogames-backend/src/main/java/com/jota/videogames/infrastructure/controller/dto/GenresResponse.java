package com.jota.videogames.infrastructure.controller.dto;

import java.util.List;

public record GenresResponse(
    List<GenreDTO> genres
) {

}
