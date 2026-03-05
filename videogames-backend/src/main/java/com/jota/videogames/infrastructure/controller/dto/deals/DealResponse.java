package com.jota.videogames.infrastructure.controller.dto.deals;

import java.util.List;

public record DealResponse(
    List<DealDTO> deals
) {

}
