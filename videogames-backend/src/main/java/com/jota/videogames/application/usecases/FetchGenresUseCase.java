package com.jota.videogames.application.usecases;

import com.jota.videogames.application.ports.IGameRepository;
import com.jota.videogames.domain.Genre;
import reactor.core.publisher.Flux;

public class FetchGenresUseCase {

    private final IGameRepository gameApiClient;

    public FetchGenresUseCase(final IGameRepository gameApiClient) {
        this.gameApiClient = gameApiClient;
    }

    public Flux<Genre> execute() {
        return this.gameApiClient.fetchGenres();
    }
}
