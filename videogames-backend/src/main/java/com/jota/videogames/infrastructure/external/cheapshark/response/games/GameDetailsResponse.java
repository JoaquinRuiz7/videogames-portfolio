package com.jota.videogames.infrastructure.external.cheapshark.response.games;

import com.jota.videogames.infrastructure.external.cheapshark.response.DealDTO;
import java.util.List;

public record GameDetailsResponse(
    GameDetailsInfo info,
    List<DealDTO> deals
) {

}
