package com.jota.videogames.application.interfaces.games;

import com.jota.videogames.domain.GameDetails;
import reactor.core.publisher.Mono;

public interface IGetGameDetailsUseCase {

    public Mono<GameDetails> getGameDetails(final Long gameId);
}
