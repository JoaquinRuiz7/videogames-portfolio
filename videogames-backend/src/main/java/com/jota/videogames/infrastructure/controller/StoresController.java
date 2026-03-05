package com.jota.videogames.infrastructure.controller;

import com.jota.videogames.application.interfaces.deals.IFindGameDealFromSpecificStoreUseCase;
import com.jota.videogames.domain.interfaces.IStoreApiClient;
import com.jota.videogames.infrastructure.controller.dto.StoresResponse;
import com.jota.videogames.infrastructure.controller.dto.deals.DealResponse;
import com.jota.videogames.infrastructure.mappers.CheapSharkMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/stores")
public class StoresController {

    private final IStoreApiClient storesService;
    private final IFindGameDealFromSpecificStoreUseCase findGameDealFromSpecificStoreUseCase;


    public StoresController(
        final IStoreApiClient stores,
        final IFindGameDealFromSpecificStoreUseCase findGameDealFromSpecificStoreUseCase
    ) {
        this.storesService = stores;
        this.findGameDealFromSpecificStoreUseCase = findGameDealFromSpecificStoreUseCase;
    }

    @GetMapping
    public Mono<StoresResponse> fetchStores() {
        return storesService.fetchStores()
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
