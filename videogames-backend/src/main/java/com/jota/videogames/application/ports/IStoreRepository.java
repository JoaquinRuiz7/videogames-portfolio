package com.jota.videogames.application.ports;

import com.jota.videogames.domain.Store;
import reactor.core.publisher.Flux;

public interface IStoreRepository {

    Flux<Store> fetchStores();

}
