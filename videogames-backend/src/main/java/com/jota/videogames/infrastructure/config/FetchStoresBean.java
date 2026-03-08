package com.jota.videogames.infrastructure.config;

import com.jota.videogames.application.ports.IStoreRepository;
import com.jota.videogames.application.usecases.FetchStoresUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FetchStoresBean {

    @Bean
    public FetchStoresUseCase fetchStoresUseCase(final IStoreRepository storeRepository) {
        return new FetchStoresUseCase(storeRepository);
    }
}
