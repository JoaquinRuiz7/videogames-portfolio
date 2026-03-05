package com.jota.videogames.application.usecases;

import com.jota.videogames.application.interfaces.deals.IFindGameDealFromSpecificStoreUseCase;
import com.jota.videogames.domain.Deal;
import com.jota.videogames.domain.Store;
import com.jota.videogames.domain.interfaces.IDealApiClient;
import com.jota.videogames.domain.interfaces.IStoreApiClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FindGameDealFromSpecificStoreImpl implements IFindGameDealFromSpecificStoreUseCase {

    private final IDealApiClient dealApiClient;
    private final IStoreApiClient storeApiClient;

    public FindGameDealFromSpecificStoreImpl(
        final IDealApiClient dealApiClient,
        final IStoreApiClient storeApiClient
    ) {
        this.dealApiClient = dealApiClient;
        this.storeApiClient = storeApiClient;

    }

    @Override
    public Flux<Deal> execute(Long gameId, Long storeId) {

        Mono<Store> storeMono = storeApiClient
            .fetchStores()
            .filter(s -> s.getId().equals(storeId))
            .singleOrEmpty();

        return dealApiClient
            .getGameDeals(gameId)
            .filter(d -> d.getStore().getId().equals(storeId))
            .flatMap(deal ->
                storeMono.map(store ->
                    new Deal(
                        gameId,
                        deal.getGame(),
                        deal.getDealId(),
                        deal.getPrice(),
                        store
                    )
                )
            );
    }
}
