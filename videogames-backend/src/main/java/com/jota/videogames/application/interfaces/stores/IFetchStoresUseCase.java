package com.jota.videogames.application.interfaces.stores;

import com.jota.videogames.domain.Store;
import reactor.core.publisher.Flux;

public interface IFetchStoresUseCase {

    public Flux<Store> fetchStores();
}
