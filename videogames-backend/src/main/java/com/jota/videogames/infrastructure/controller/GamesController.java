package com.jota.videogames.infrastructure.controller;

import com.jota.videogames.application.interfaces.deals.IFindGameDealFromSpecificStoreUseCase;
import com.jota.videogames.application.interfaces.deals.IFindGameDealUseCase;
import com.jota.videogames.application.interfaces.games.IGetGameDetailsUseCase;
import com.jota.videogames.application.interfaces.games.IGetGamesUseCase;
import com.jota.videogames.domain.GameDetails;
import com.jota.videogames.infrastructure.controller.dto.GetGamesResponse;
import com.jota.videogames.infrastructure.controller.dto.deals.DealResponse;
import com.jota.videogames.infrastructure.mappers.CheapSharkMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/games")
public class GamesController {

    private final IGetGamesUseCase getGamesUseCase;
    private final IFindGameDealUseCase findGameDealUseCase;
    private final IGetGameDetailsUseCase getGameDetailsUseCase;

    public GamesController(
        final IGetGamesUseCase getGamesUseCase,
        final IFindGameDealUseCase findGameDealUseCase,
        final IGetGameDetailsUseCase getGameDetailsUseCase,
        final IFindGameDealFromSpecificStoreUseCase findGameDealFromSpecificStoreUseCase
    ) {
        this.getGamesUseCase = getGamesUseCase;
        this.findGameDealUseCase = findGameDealUseCase;
        this.getGameDetailsUseCase = getGameDetailsUseCase;
    }

    @GetMapping
    public Mono<GetGamesResponse> getGames(
        @RequestParam(defaultValue = "") final String search,
        @RequestParam(value = "exact", defaultValue = "true", required = false) final String exact,
        @RequestParam(required = false) final String page,
        @RequestParam(required = false, defaultValue = "20") final Integer limit
    ) {
        return this.getGamesUseCase.execute(search, exact, page, limit)
            .map(games -> new GetGamesResponse(
                games.nextPage(),
                games.previousPage(),
                games.games()
            ));
    }

    @GetMapping("/{game}/deals")
    public Mono<DealResponse> getDeal(@PathVariable final String game) {
        return this.findGameDealUseCase.execute(game)
            .map(CheapSharkMapper::toDealDTO)
            .collectList()
            .map(DealResponse::new);
    }

    @GetMapping("/{gameId}")
    public Mono<GameDetails> getGameDetails(@PathVariable final Long gameId) {
        return this.getGameDetailsUseCase.getGameDetails(gameId);
    }
}
