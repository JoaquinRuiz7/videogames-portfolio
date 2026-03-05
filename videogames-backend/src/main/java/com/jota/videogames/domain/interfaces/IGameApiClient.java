package com.jota.videogames.domain.interfaces;

import com.jota.videogames.domain.GameDetails;
import com.jota.videogames.domain.GamesPage;
import com.jota.videogames.domain.Genre;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IGameApiClient {

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
