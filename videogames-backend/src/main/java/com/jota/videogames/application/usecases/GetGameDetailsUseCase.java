package com.jota.videogames.application.usecases;

import com.jota.videogames.application.ports.IGameRepository;
import com.jota.videogames.domain.GameDetails;
import reactor.core.publisher.Mono;

public class GetGameDetailsUseCase {

    private final IGameRepository gameRepository;

    public GetGameDetailsUseCase(final IGameRepository gameApiClient) {
        this.gameRepository = gameApiClient;
    }

    public Mono<GameDetails> getGameDetails(final Long gameId) {
        return gameRepository.getGameDetails(gameId);
    }
}
