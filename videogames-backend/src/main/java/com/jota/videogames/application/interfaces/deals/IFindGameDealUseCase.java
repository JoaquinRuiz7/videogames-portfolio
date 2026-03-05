package com.jota.videogames.application.interfaces.deals;

import com.jota.videogames.domain.Deal;
import reactor.core.publisher.Flux;

public interface IFindGameDealUseCase {

    public Flux<Deal> execute(final String name);
}
