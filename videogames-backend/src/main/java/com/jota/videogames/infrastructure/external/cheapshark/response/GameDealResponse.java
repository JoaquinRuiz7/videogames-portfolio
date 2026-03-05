package com.jota.videogames.infrastructure.external.cheapshark.response;

import com.jota.videogames.infrastructure.external.cheapshark.response.games.GameInfo;
import java.util.List;

public record GameDealResponse(
    GameInfo gameInfo,
    List<CheaperStore> cheaperStores,
    Double price,
    String store
) {

}
