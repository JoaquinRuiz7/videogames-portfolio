package com.jota.videogames.application.interfaces;

import com.jota.videogames.domain.Genre;
import reactor.core.publisher.Flux;

public interface IFetchGenresUseCase {

    public Flux<Genre> execute();
}
