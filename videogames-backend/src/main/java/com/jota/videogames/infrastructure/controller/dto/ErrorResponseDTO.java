package com.jota.videogames.infrastructure.controller.dto;

import java.time.Instant;

public record ErrorResponseDTO(
    int status,
    String message,
    Instant timestamp
) {

}
