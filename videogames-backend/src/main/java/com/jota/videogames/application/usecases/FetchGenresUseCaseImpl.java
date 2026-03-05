package com.jota.videogames.application.usecases;

import com.jota.videogames.application.interfaces.IFetchGenresUseCase;
import com.jota.videogames.domain.Genre;
import com.jota.videogames.domain.interfaces.IGameApiClient;
import reactor.core.publisher.Flux;

public class FetchGenresUseCaseImpl implements IFetchGenresUseCase {

    private final IGameApiClient gameApiClient;

    public FetchGenresUseCaseImpl(final IGameApiClient gameApiClient) {
        this.gameApiClient = gameApiClient;
    }

    @Override
    public Flux<Genre> execute() {
        return this.gameApiClient.fetchGenres();
    }
}
