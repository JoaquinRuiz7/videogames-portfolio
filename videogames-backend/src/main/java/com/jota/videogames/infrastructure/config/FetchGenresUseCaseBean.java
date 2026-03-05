package com.jota.videogames.infrastructure.config;

import com.jota.videogames.application.interfaces.IFetchGenresUseCase;
import com.jota.videogames.application.interfaces.games.IGetGameDetailsUseCase;
import com.jota.videogames.application.usecases.FetchGenresUseCaseImpl;
import com.jota.videogames.application.usecases.GetGameDetailsUseCase;
import com.jota.videogames.domain.interfaces.IGameApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FetchGenresUseCaseBean {

    @Bean
    public IFetchGenresUseCase fetchGenresUseCase(final IGameApiClient gameApiClient) {
        return new FetchGenresUseCaseImpl(gameApiClient);
    }

    @Bean
    public IGetGameDetailsUseCase getGameDetailsUseCase(final IGameApiClient gameApiClient) {
        return new GetGameDetailsUseCase(gameApiClient);
    }
}
