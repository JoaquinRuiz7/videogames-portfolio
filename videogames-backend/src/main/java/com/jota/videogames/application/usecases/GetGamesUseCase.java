package com.jota.videogames.application.usecases;

import com.jota.videogames.application.ports.IGameRepository;
import com.jota.videogames.domain.Game;
import com.jota.videogames.domain.GamesPage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class GetGamesUseCase {

    private final IGameRepository gameRepository;

    public GetGamesUseCase(IGameRepository gameApiClient) {
        this.gameRepository = gameApiClient;
    }


    public Mono<GamesPage> execute(
            String search,
            String exact,
            String page,
            Integer limit
    ) {
        return gameRepository.fetchGames(search, exact, page, limit)
                .flatMap(g ->
                        Flux.fromIterable(g.games())
                                .flatMap(game ->
                                        gameRepository.getGameScreenshots(game.getId())
                                                .map(screenshots ->
                                                        new Game(
                                                                game.getId(),
                                                                game.getName(),
                                                                game.getThumbnail(),
                                                                game.getRating(),
                                                                game.getMetaCritic(),
                                                                game.getEsrb(),
                                                                game.getPlatforms(),
                                                                game.getReleased(),
                                                                screenshots
                                                        )
                                                )
                                )
                                .collectList()
                                .map(updatedGames ->
                                        new GamesPage(
                                                g.nextPage(),
                                                g.previousPage(),
                                                updatedGames
                                        )
                                )
                );
    }
}
