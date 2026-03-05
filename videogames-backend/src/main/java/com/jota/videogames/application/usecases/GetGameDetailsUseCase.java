package com.jota.videogames.application.usecases;

import com.jota.videogames.application.interfaces.games.IGetGameDetailsUseCase;
import com.jota.videogames.domain.GameDetails;
import com.jota.videogames.domain.interfaces.IGameApiClient;
import reactor.core.publisher.Mono;

public class GetGameDetailsUseCase implements IGetGameDetailsUseCase {

    private final IGameApiClient gameApiClient;

    public GetGameDetailsUseCase(final IGameApiClient gameApiClient) {
        this.gameApiClient = gameApiClient;
    }

    @Override
    public Mono<GameDetails> getGameDetails(final Long gameId) {
        return gameApiClient.getGameDetails(gameId);
    }
}
