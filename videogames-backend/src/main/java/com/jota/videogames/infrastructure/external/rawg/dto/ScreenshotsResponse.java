package com.jota.videogames.infrastructure.external.rawg.dto;

import java.util.List;

public record ScreenshotsResponse(
    List<Screenshot> results
) {

}
