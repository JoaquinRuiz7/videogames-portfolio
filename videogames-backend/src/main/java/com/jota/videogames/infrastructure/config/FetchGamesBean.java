package com.jota.videogames.infrastructure.config;

import com.jota.videogames.application.interfaces.games.IGetGamesUseCase;
import com.jota.videogames.application.usecases.GetGamesUseCaseImpl;
import com.jota.videogames.domain.interfaces.IGameApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FetchGamesBean {

    @Bean
    public IGetGamesUseCase iGetGamesUseCase(IGameApiClient gameApiClient) {
        return new GetGamesUseCaseImpl(gameApiClient);
    }

}
