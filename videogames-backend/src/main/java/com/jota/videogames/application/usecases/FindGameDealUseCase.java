package com.jota.videogames.application.usecases;

import com.jota.videogames.application.ports.IDealRepository;
import com.jota.videogames.application.ports.IStoreRepository;
import com.jota.videogames.domain.Deal;
import reactor.core.publisher.Flux;

public class FindGameDealUseCase {

    private final IDealRepository dealApiClient;
    private final IStoreRepository storeApiClient;

    public FindGameDealUseCase(
            final IDealRepository dealApiClient,
            final IStoreRepository storeApiClient
    ) {
        this.dealApiClient = dealApiClient;
        this.storeApiClient = storeApiClient;
    }

    public Flux<Deal> execute(String name) {
        return dealApiClient
                .getGameCheapestDeal(name, 1)
                .switchIfEmpty(dealApiClient.getGameCheapestDeal(name, 0))
                .next()
                .flatMap(deal ->
                        dealApiClient.getDealById(deal.getDealId())
                )
                .flatMap(detailedDeal ->
                        storeApiClient
                                .fetchStores()
                                .filter(store -> store.getId().equals(detailedDeal.getStore().getId()))
                                .next()
                                .map(store -> new Deal(
                                                detailedDeal.getGameId(),
                                                detailedDeal.getGame(),
                                                detailedDeal.getDealId(),
                                                detailedDeal.getPrice(),
                                                store
                                        )
                                )
                                .defaultIfEmpty(detailedDeal)
                )
                .flux();
    }
}
