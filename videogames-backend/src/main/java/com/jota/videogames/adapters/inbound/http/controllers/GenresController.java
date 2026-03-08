package com.jota.videogames.adapters.inbound.http.controllers;

import com.jota.videogames.adapters.inbound.http.dto.genres.GenreResponse;
import com.jota.videogames.adapters.inbound.http.dto.genres.GenresResponse;
import com.jota.videogames.application.usecases.FetchGenresUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/genres")
public class GenresController {

    private final FetchGenresUseCase fetchGenresUseCase;

    public GenresController(final FetchGenresUseCase fetchGenresUseCase) {
        this.fetchGenresUseCase = fetchGenresUseCase;
    }

    @GetMapping
    public Mono<GenresResponse> fetchGenres() {
        return this.fetchGenresUseCase
                .execute()
                .map(genre -> new GenreResponse(
                        genre.getName(),
                        genre.getId(),
                        genre.getImage()
                ))
                .collectList()
                .map(GenresResponse::new);
    }
}
