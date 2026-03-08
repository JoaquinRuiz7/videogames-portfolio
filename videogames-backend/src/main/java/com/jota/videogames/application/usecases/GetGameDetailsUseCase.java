package com.jota.videogames.application.usecases;

import com.jota.videogames.application.ports.IGameRepository;
import com.jota.videogames.domain.GameDetails;
import reactor.core.publisher.Mono;

public class GetGameDetailsUseCase {

    private final IGameRepository gameApiClient;

    public GetGameDetailsUseCase(final IGameRepository gameApiClient) {
        this.gameApiClient = gameApiClient;
    }

    public Mono<GameDetails> getGameDetails(final Long gameId) {
        return gameApiClient.getGameDetails(gameId);
    }
}
