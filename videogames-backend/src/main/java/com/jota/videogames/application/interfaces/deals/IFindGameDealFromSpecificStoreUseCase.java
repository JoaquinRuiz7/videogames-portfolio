package com.jota.videogames.application.interfaces.deals;

import com.jota.videogames.domain.Deal;
import reactor.core.publisher.Flux;

public interface IFindGameDealFromSpecificStoreUseCase {

    public Flux<Deal> execute(final Long gameId, final Long storeId);
}
