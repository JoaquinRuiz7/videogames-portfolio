package com.jota.videogames.domain.interfaces;

import com.jota.videogames.domain.Store;
import reactor.core.publisher.Flux;

public interface IStoreApiClient {

    Flux<Store> fetchStores();

}
