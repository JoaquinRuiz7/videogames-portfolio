package com.jota.videogames.application.usecases;

import com.jota.videogames.application.interfaces.games.IGetGamesUseCase;
import com.jota.videogames.domain.Game;
import com.jota.videogames.domain.GamesPage;
import com.jota.videogames.domain.interfaces.IGameApiClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class GetGamesUseCaseImpl implements IGetGamesUseCase {

    private final IGameApiClient gameApiClient;

    public GetGamesUseCaseImpl(IGameApiClient gameApiClient) {
        this.gameApiClient = gameApiClient;
    }


    @Override
    public Mono<GamesPage> execute(
        String search,
        String exact,
        String page,
        Integer limit
    ) {
        return gameApiClient.fetchGames(search, exact, page, limit)
            .flatMap(g ->
                Flux.fromIterable(g.games())
                    .flatMap(game ->
                        gameApiClient.getGameScreenshots(game.getId())
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
