package com.jota.videogames.adapters.inbound.http.dto;

import java.time.Instant;

public record ErrorResponseDTO(
        int status,
        String message,
        Instant timestamp
) {

}
