package com.jota.videogames.adapters.inbound.http.controllers;

import com.jota.videogames.adapters.inbound.http.dto.deals.DealResponse;
import com.jota.videogames.adapters.inbound.http.dto.games.GetGamesResponse;
import com.jota.videogames.application.usecases.FindGameDealUseCase;
import com.jota.videogames.application.usecases.GetGameDetailsUseCase;
import com.jota.videogames.application.usecases.GetGamesUseCase;
import com.jota.videogames.domain.GameDetails;
import com.jota.videogames.infrastructure.mappers.CheapSharkMapper;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/games")
public class GamesController {

    private final GetGamesUseCase getGamesUseCase;
    private final FindGameDealUseCase findGameDealUseCase;
    private final GetGameDetailsUseCase getGameDetailsUseCase;

    public GamesController(
            final GetGamesUseCase getGamesUseCase,
            final FindGameDealUseCase findGameDealUseCase,
            final GetGameDetailsUseCase getGameDetailsUseCase
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
