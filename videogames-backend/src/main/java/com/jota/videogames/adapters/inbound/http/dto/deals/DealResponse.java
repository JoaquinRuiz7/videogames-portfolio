package com.jota.videogames.adapters.inbound.http.dto.deals;

import java.util.List;

public record DealResponse(
        List<DealDTO> deals
) {

}
