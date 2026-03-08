package com.jota.videogames.infrastructure.config;

import com.jota.videogames.application.ports.IDealRepository;
import com.jota.videogames.application.ports.IStoreRepository;
import com.jota.videogames.application.usecases.FindGameDealFromSpecificStore;
import com.jota.videogames.application.usecases.FindGameDealUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FindGameDealConfig {

    @Bean
    public FindGameDealUseCase iFindGameDealUseCase(
            final IDealRepository dealApiClient,
            final IStoreRepository storeApiClient
    ) {
        return new FindGameDealUseCase(dealApiClient, storeApiClient);
    }

    @Bean
    public FindGameDealFromSpecificStore findGameDealFromSpecificStoreUseCase(
            final IDealRepository dealApiClient, final IStoreRepository storeApiClient) {
        return new FindGameDealFromSpecificStore(dealApiClient, storeApiClient);
    }

}
