package com.jota.videogames.adapters.inbound.http.controllers;

import com.jota.videogames.adapters.inbound.http.dto.deals.DealResponse;
import com.jota.videogames.adapters.inbound.http.dto.stores.StoresResponse;
import com.jota.videogames.application.usecases.FetchStoresUseCase;
import com.jota.videogames.application.usecases.FindGameDealFromSpecificStore;
import com.jota.videogames.infrastructure.mappers.CheapSharkMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/stores")
public class StoresController {

    private final FetchStoresUseCase fetchStoresUseCase;
    private final FindGameDealFromSpecificStore findGameDealFromSpecificStoreUseCase;


    public StoresController(
            final FetchStoresUseCase fetchStoresUseCase,
            final FindGameDealFromSpecificStore findGameDealFromSpecificStoreUseCase
    ) {
        this.fetchStoresUseCase = fetchStoresUseCase;
        this.findGameDealFromSpecificStoreUseCase = findGameDealFromSpecificStoreUseCase;
    }

    @GetMapping
    public Mono<StoresResponse> fetchStores() {
        return fetchStoresUseCase.execute()
                .collectList()
                .map(StoresResponse::new);
    }

    @GetMapping("/{storeId}/games/{gameId}/deals")
    public Mono<DealResponse> getDealForSpecificGameInSpecificStore(
            final @PathVariable Long storeId,
            final @PathVariable Long gameId
    ) {
        return this.findGameDealFromSpecificStoreUseCase.execute(gameId, storeId)
                .map(CheapSharkMapper::toDealDTO)
                .collectList()
                .map(DealResponse::new);
    }

}
