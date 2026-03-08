package com.jota.videogames.infrastructure.mappers;

import com.jota.videogames.adapters.inbound.http.dto.deals.DealDTO;
import com.jota.videogames.adapters.inbound.http.dto.deals.StoreResponse;
import com.jota.videogames.domain.Deal;
import com.jota.videogames.domain.Store;
import com.jota.videogames.infrastructure.external.cheapshark.response.CheapSharkGame;
import com.jota.videogames.infrastructure.external.cheapshark.response.stores.StoreDTO;

public class CheapSharkMapper {

    private static final String DEAL_URL_PATH = "https://www.cheapshark.com/redirect?dealID=";

    public static Deal toDomainDeal(final CheapSharkGame cheapSharkGame) {
        return new Deal(
                cheapSharkGame.gameId(),
                cheapSharkGame.externalName(),
                cheapSharkGame.cheapestDealId(),
                cheapSharkGame.cheapestPrice()
        );
    }

    public static DealDTO toDealDTO(final Deal deal) {
        return new DealDTO(
                DEAL_URL_PATH.concat(deal.getDealId()),
                deal.getPrice(),
                deal.getGame(),
                new StoreResponse(
                        deal.getStore().getName(),
                        deal.getStore().getLogo()
                )
        );
    }

    public static Store toDomainStore(final StoreDTO storeDTO) {
        return new Store(
                Long.valueOf(storeDTO.storeID()),
                storeDTO.storeName(),
                storeDTO.images().icon()
        );
    }
}
