package com.jota.videogames.infrastructure.external.rawg;

import com.jota.videogames.domain.Game;
import com.jota.videogames.domain.GameDetails;
import com.jota.videogames.domain.GamesPage;
import com.jota.videogames.domain.Genre;
import com.jota.videogames.domain.interfaces.IGameApiClient;
import com.jota.videogames.infrastructure.external.rawg.dto.FetchGamesResponse;
import com.jota.videogames.infrastructure.external.rawg.dto.GenreResponse;
import com.jota.videogames.infrastructure.external.rawg.dto.RawgGameDetails;
import com.jota.videogames.infrastructure.external.rawg.dto.RawgPublisher;
import com.jota.videogames.infrastructure.external.rawg.dto.Screenshot;
import com.jota.videogames.infrastructure.external.rawg.dto.ScreenshotsResponse;
import com.jota.videogames.infrastructure.mappers.RawgMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class RawgApiClient implements IGameApiClient {

    private static final String GAMES_PATH = "/games";
    private static final String GENRES_PATh = "/genres";
    private static final String SCREENSHOTS_PATH = "/games/{gameId}/screenshots";
    private static final String GAME_DETAILS_PATH = "/games/{gameId}";
    private final WebClient rawgClient;

    @Value("${rawg.api.key}")
    private String key;

    public RawgApiClient(@Qualifier("rawgClient") final WebClient rawgClient) {
        this.rawgClient = rawgClient;
    }

    public Mono<GamesPage> fetchGames(
        final String title,
        final String exact,
        final String page,
        final Integer limit
    ) {

        if (page != null && !page.isEmpty()) {
            return rawgClient
                .get()
                .uri(page)
                .retrieve()
                .bodyToMono(FetchGamesResponse.class)
                .map(this::toDomain);
        }

        return rawgClient
            .get()
            .uri(uriBuilder -> uriBuilder
                .path(GAMES_PATH)
                .queryParam("key", key)
                .queryParam("search", title)
                .queryParam("search_exact", exact)
                .queryParam("page_size", limit)
                .build())
            .retrieve()
            .bodyToMono(FetchGamesResponse.class)
            .map(this::toDomain);
    }

    @Override
    public Flux<Genre> fetchGenres() {
        return rawgClient
            .get()
            .uri(uriBuilder -> uriBuilder
                .path(GENRES_PATh)
                .queryParam("key", key)
                .build()
            ).retrieve()
            .bodyToMono(GenreResponse.class)
            .flatMapMany(
                (GenreResponse genreResponse) -> Flux.fromIterable(genreResponse.results())
                    .map(RawgMapper::toDomainGenre)
            );
    }

    @Override
    public Mono<List<String>> getGameScreenshots(final Long gameId) {
        return rawgClient
            .get()
            .uri(uriBuilder ->
                uriBuilder
                    .path(SCREENSHOTS_PATH.replace("{gameId}", gameId + ""))
                    .queryParam("key", key)
                    .build()
            )
            .retrieve()
            .bodyToMono(ScreenshotsResponse.class)
            .map(screenshotsResponse -> screenshotsResponse
                .results()
                .stream()
                .map(Screenshot::image)
                .toList()
            );
    }

    @Override
    public Mono<GameDetails> getGameDetails(final Long gameId) {
        return rawgClient
            .get()
            .uri(uriBuilder ->
                uriBuilder
                    .path(GAME_DETAILS_PATH.replace("{gameId}", gameId + ""))
                    .queryParam("key", key)
                    .build()
            )
            .retrieve()
            .bodyToMono(RawgGameDetails.class)
            .map(rawgGameDetails
                    -> new GameDetails(
                    rawgGameDetails.description(),
                    rawgGameDetails.playtime(),
                    rawgGameDetails.metacriticUrl(),
                    rawgGameDetails.publishers()
                        .stream()
                        .map(RawgPublisher::name).toList()
                )
            );
    }

    private GamesPage toDomain(final FetchGamesResponse response) {

        List<Game> games = response.results()
            .stream()
            .map(RawgMapper::toDomainGame)
            .toList();

        return new GamesPage(
            response.next(),
            response.previous(),
            games
        );
    }

}
