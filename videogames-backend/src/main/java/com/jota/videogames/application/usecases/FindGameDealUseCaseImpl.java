package com.jota.videogames.application.usecases;

import com.jota.videogames.application.interfaces.deals.IFindGameDealUseCase;
import com.jota.videogames.domain.Deal;
import com.jota.videogames.domain.interfaces.IDealApiClient;
import com.jota.videogames.domain.interfaces.IStoreApiClient;
import reactor.core.publisher.Flux;

public class FindGameDealUseCaseImpl implements IFindGameDealUseCase {

    private final IDealApiClient dealApiClient;
    private final IStoreApiClient storeApiClient;

    public FindGameDealUseCaseImpl(
        final IDealApiClient dealApiClient,
        final IStoreApiClient storeApiClient
    ) {
        this.dealApiClient = dealApiClient;
        this.storeApiClient = storeApiClient;
    }

    @Override
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
