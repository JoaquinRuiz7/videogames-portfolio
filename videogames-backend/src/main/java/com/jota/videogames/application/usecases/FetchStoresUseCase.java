package com.jota.videogames.application.usecases;

import com.jota.videogames.application.ports.IStoreRepository;
import com.jota.videogames.domain.Store;
import reactor.core.publisher.Flux;

public class FetchStoresUseCase {

    private final IStoreRepository storeRepository;

    public FetchStoresUseCase(final IStoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public Flux<Store> execute() {
        return storeRepository.fetchStores();
    }
}
