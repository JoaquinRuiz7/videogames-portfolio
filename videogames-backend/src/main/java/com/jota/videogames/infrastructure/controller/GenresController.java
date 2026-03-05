package com.jota.videogames.infrastructure.controller;

import com.jota.videogames.application.interfaces.IFetchGenresUseCase;
import com.jota.videogames.infrastructure.controller.dto.GenreDTO;
import com.jota.videogames.infrastructure.controller.dto.GenresResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/genres")
public class GenresController {

    private final IFetchGenresUseCase fetchGenresUseCase;

    public GenresController(final IFetchGenresUseCase fetchGenresUseCase) {
        this.fetchGenresUseCase = fetchGenresUseCase;
    }

    @GetMapping
    public Mono<GenresResponse> fetchGenres() {
        return this.fetchGenresUseCase
            .execute()
            .map(genre -> new GenreDTO(
                genre.getName(),
                genre.getId(),
                genre.getImage()
            ))
            .collectList()
            .map(GenresResponse::new);
    }
}
