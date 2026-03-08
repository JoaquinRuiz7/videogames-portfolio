package com.jota.videogames.infrastructure.config;

import com.jota.videogames.application.ports.IGameRepository;
import com.jota.videogames.application.usecases.GetGamesUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FetchGamesBean {

    @Bean
    public GetGamesUseCase iGetGamesUseCase(IGameRepository gameApiClient) {
        return new GetGamesUseCase(gameApiClient);
    }

}
