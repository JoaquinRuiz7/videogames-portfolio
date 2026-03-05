package com.jota.videogames.infrastructure.mappers;

import com.jota.videogames.domain.ESRB;
import com.jota.videogames.domain.Game;
import com.jota.videogames.domain.Genre;
import com.jota.videogames.infrastructure.external.rawg.dto.ESRBRating;
import com.jota.videogames.infrastructure.external.rawg.dto.RawgGameResponse;
import com.jota.videogames.infrastructure.external.rawg.dto.RawgGenre;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RawgMapper {

    public static Game toDomainGame(final RawgGameResponse game) {
        return new Game(
            game.id(),
            game.name(),
            game.backgroundImage(),
            game.rating(),
            game.metacritic(),
            Optional
                .ofNullable(game.esrbRating())
                .map(ESRBRating::name)
                .map(ESRB::fromName)
                .orElse(ESRB.RP),
            Optional
                .ofNullable(game.platforms())
                .orElse(List.of())
                .stream()
                .map(p -> p.platform().name())
                .toList(),
            game.released(),
            new ArrayList<String>(),
            null
        );
    }

    public static Genre toDomainGenre(final RawgGenre rawgGenre) {
        return new Genre(
            rawgGenre.id(),
            rawgGenre.name(),
            rawgGenre.imageBackground()
        );
    }
}
