package com.jota.videogames.infrastructure.config;

import com.jota.videogames.application.ports.IGameRepository;
import com.jota.videogames.application.usecases.FetchGenresUseCase;
import com.jota.videogames.application.usecases.GetGameDetailsUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FetchGenresUseCaseBean {

    @Bean
    public FetchGenresUseCase fetchGenresUseCase(final IGameRepository gameApiClient) {
        return new FetchGenresUseCase(gameApiClient);
    }

    @Bean
    public GetGameDetailsUseCase getGameDetailsUseCase(final IGameRepository gameApiClient) {
        return new GetGameDetailsUseCase(gameApiClient);
    }
}
