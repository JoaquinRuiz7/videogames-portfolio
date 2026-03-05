package com.jota.videogames.infrastructure.config;

import com.jota.videogames.application.interfaces.deals.IFindGameDealFromSpecificStoreUseCase;
import com.jota.videogames.application.interfaces.deals.IFindGameDealUseCase;
import com.jota.videogames.application.usecases.FindGameDealFromSpecificStoreImpl;
import com.jota.videogames.application.usecases.FindGameDealUseCaseImpl;
import com.jota.videogames.domain.interfaces.IDealApiClient;
import com.jota.videogames.domain.interfaces.IStoreApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FindGameDealConfig {

    @Bean
    public IFindGameDealUseCase iFindGameDealUseCase(
        final IDealApiClient dealApiClient,
        final IStoreApiClient storeApiClient
    ) {
        return new FindGameDealUseCaseImpl(dealApiClient, storeApiClient);
    }

    @Bean
    public IFindGameDealFromSpecificStoreUseCase findGameDealFromSpecificStoreUseCase(
        final IDealApiClient dealApiClient, final IStoreApiClient storeApiClient) {
        return new FindGameDealFromSpecificStoreImpl(dealApiClient, storeApiClient);
    }

}
