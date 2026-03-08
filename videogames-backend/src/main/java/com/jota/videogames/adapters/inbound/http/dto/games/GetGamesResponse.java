package com.jota.videogames.adapters.inbound.http.dto.games;

import com.jota.videogames.domain.Game;

import java.util.List;

public record GetGamesResponse(
        String next,
        String previous,
        List<Game> games
) {

}
