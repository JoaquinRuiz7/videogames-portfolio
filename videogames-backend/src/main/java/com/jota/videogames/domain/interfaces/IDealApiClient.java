package com.jota.videogames.domain.interfaces;

import com.jota.videogames.domain.Deal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IDealApiClient {

    Flux<Deal> getGameCheapestDeal(final String title, final int exact);

    Flux<Deal> getGameDeals(final Long gameId);

    Mono<Deal> getDealById(final String dealId);

}
