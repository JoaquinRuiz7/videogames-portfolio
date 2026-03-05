package com.jota.videogames.application.interfaces.games;

import com.jota.videogames.domain.GamesPage;
import reactor.core.publisher.Mono;

public interface IGetGamesUseCase {

    public Mono<GamesPage> execute(
        final String search,
        final String exact,
        final String page,
        final Integer limit
    );
}
