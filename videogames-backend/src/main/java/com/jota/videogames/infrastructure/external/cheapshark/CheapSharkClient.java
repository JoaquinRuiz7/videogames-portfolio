package com.jota.videogames.infrastructure.external.cheapshark;

import com.jota.videogames.domain.Deal;
import com.jota.videogames.domain.Store;
import com.jota.videogames.domain.interfaces.IDealApiClient;
import com.jota.videogames.domain.interfaces.IStoreApiClient;
import com.jota.videogames.infrastructure.external.cheapshark.response.CheapSharkGame;
import com.jota.videogames.infrastructure.external.cheapshark.response.deals.DealResponse;
import com.jota.videogames.infrastructure.external.cheapshark.response.games.GameDetailsResponse;
import com.jota.videogames.infrastructure.external.cheapshark.response.stores.StoreDTO;
import com.jota.videogames.infrastructure.mappers.CheapSharkMapper;
import java.net.URI;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CheapSharkClient implements IDealApiClient, IStoreApiClient {

    private static final String GAMES_PATH = "/games";
    private static final String DEALS_PATH = "/deals";
    private static final String STORES_PATH = "/stores";

    @Value("${cheapshark.base.url}")
    private String baseUrl;

    private final WebClient cheapSharkWebClient;

    public CheapSharkClient(final @Qualifier("cheapSharkWebClient") WebClient cheapSharkWebClient) {
        this.cheapSharkWebClient = cheapSharkWebClient;
    }

    @Override
    public Flux<Deal> getGameCheapestDeal(final String title, final int exact) {
        return this.cheapSharkWebClient
            .get()
            .uri(uriBuilder -> {
                return uriBuilder
                    .path(GAMES_PATH)
                    .queryParam("title", title)
                    .queryParam("exact", exact)
                    .build();
            })
            .retrieve()
            .bodyToFlux(CheapSharkGame.class)
            .map(CheapSharkMapper::toDomainDeal);
    }

    @Override
    public Flux<Deal> getGameDeals(final Long gameId) {
        return this.cheapSharkWebClient
            .get()
            .uri(uriBuilder ->
                uriBuilder
                    .path(GAMES_PATH)
                    .queryParam("id", String.valueOf(gameId))
                    .build()
            )
            .retrieve()
            .bodyToMono(GameDetailsResponse.class)
            .flatMapMany(response ->
                Flux.fromIterable(response.deals())
                    .map(dealDTO ->
                        new Deal(
                            gameId,
                            dealDTO.dealID(),
                            response.info().title(),
                            dealDTO.price()
                        )
                    )
            );
    }

    @Override
    public Mono<Deal> getDealById(final String dealId) {
        final URI uri = URI.create(
            String.format("%s%s%s%s", baseUrl, DEALS_PATH, "?id=", dealId)
        );
        return cheapSharkWebClient
            .get()
            .uri(uri)
            .retrieve()
            .bodyToMono(DealResponse.class)
            .map(dealResponse ->
                new Deal(
                    dealResponse.gameInfo().gameId(),
                    dealResponse.gameInfo().name(),
                    dealId,
                    dealResponse.gameInfo().salePrice(),
                    new Store(dealResponse.gameInfo().storeId())
                )
            );
    }

    @Override
    @Cacheable("stores")
    public Flux<Store> fetchStores() {
        return cheapSharkWebClient
            .get()
            .uri(uriBuilder ->
                uriBuilder
                    .path(STORES_PATH)
                    .build()
            )
            .retrieve()
            .bodyToFlux(StoreDTO.class)
            .map(CheapSharkMapper::toDomainStore);
    }
}
