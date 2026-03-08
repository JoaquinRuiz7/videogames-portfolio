package com.jota.videogames.application.ports;

import com.jota.videogames.domain.Deal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IDealRepository {

    Flux<Deal> getGameCheapestDeal(final String title, final int exact);

    Flux<Deal> getGameDeals(final Long gameId);

    Mono<Deal> getDealById(final String dealId);

}
