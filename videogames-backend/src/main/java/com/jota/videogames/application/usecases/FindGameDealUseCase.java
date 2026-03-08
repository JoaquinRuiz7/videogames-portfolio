package com.jota.videogames.application.usecases;

import com.jota.videogames.application.ports.IDealRepository;
import com.jota.videogames.application.ports.IStoreRepository;
import com.jota.videogames.domain.Deal;
import reactor.core.publisher.Flux;

public class FindGameDealUseCase {

    private final IDealRepository dealRepository;
    private final IStoreRepository storeRepository;

    public FindGameDealUseCase(
            final IDealRepository dealApiClient,
            final IStoreRepository storeApiClient
    ) {
        this.dealRepository = dealApiClient;
        this.storeRepository = storeApiClient;
    }

    public Flux<Deal> execute(String name) {
        return dealRepository
                .getGameCheapestDeal(name, 1)
                .switchIfEmpty(dealRepository.getGameCheapestDeal(name, 0))
                .next()
                .flatMap(deal ->
                        dealRepository.getDealById(deal.getDealId())
                )
                .flatMap(detailedDeal ->
                        storeRepository
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
