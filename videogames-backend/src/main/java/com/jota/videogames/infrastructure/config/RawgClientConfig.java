package com.jota.videogames.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RawgClientConfig {

    @Value("${rawg.base.url}")
    private String baseUrl;

    @Bean
    public WebClient rawgClient(WebClient.Builder builder) {
        return builder
            .baseUrl(baseUrl)
            .build();
    }
}
