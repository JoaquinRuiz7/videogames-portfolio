package com.jota.videogames.application.ports;

import com.jota.videogames.domain.GameDetails;
import com.jota.videogames.domain.GamesPage;
import com.jota.videogames.domain.Genre;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IGameRepository {

    public Mono<GamesPage> fetchGames(
            final String title,
            final String exact,
            final String page,
            final Integer limit
    );

    public Flux<Genre> fetchGenres();

    public Mono<List<String>> getGameScreenshots(final Long gameId);

    public Mono<GameDetails> getGameDetails(final Long gameId);

}
