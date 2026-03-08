package com.jota.videogames.application.usecases;

import com.jota.videogames.application.ports.IDealRepository;
import com.jota.videogames.application.ports.IStoreRepository;
import com.jota.videogames.domain.Deal;
import com.jota.videogames.domain.Store;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FindGameDealFromSpecificStore {

    private final IDealRepository dealRepository;
    private final IStoreRepository storeRepository;

    public FindGameDealFromSpecificStore(
            final IDealRepository dealApiClient,
            final IStoreRepository storeApiClient
    ) {
        this.dealRepository = dealApiClient;
        this.storeRepository = storeApiClient;

    }

    public Flux<Deal> execute(Long gameId, Long storeId) {

        Mono<Store> storeMono = storeRepository
                .fetchStores()
                .filter(s -> s.getId().equals(storeId))
                .singleOrEmpty();

        return dealRepository
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
