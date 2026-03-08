package com.jota.videogames.application.usecases;

import com.jota.videogames.application.ports.IStoreRepository;
import com.jota.videogames.domain.Store;
import reactor.core.publisher.Flux;

public class FetchStoresUseCase {

    private final IStoreRepository storeApiClient;

    public FetchStoresUseCase(final IStoreRepository storeApiClient) {
        this.storeApiClient = storeApiClient;
    }

    public Flux<Store> execute() {
        return storeApiClient.fetchStores();
    }
}
