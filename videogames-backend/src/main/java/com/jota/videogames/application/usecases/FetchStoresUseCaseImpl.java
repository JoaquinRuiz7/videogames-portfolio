package com.jota.videogames.application.usecases;

import com.jota.videogames.application.interfaces.stores.IFetchStoresUseCase;
import com.jota.videogames.domain.Store;
import com.jota.videogames.domain.interfaces.IStoreApiClient;
import reactor.core.publisher.Flux;

public class FetchStoresUseCaseImpl implements IFetchStoresUseCase {

    private final IStoreApiClient storeApiClient;

    public FetchStoresUseCaseImpl(final IStoreApiClient storeApiClient) {
        this.storeApiClient = storeApiClient;
    }

    @Override
    public Flux<Store> fetchStores() {
        return storeApiClient.fetchStores();
    }
}
