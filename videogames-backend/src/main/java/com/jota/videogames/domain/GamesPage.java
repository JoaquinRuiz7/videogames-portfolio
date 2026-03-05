package com.jota.videogames.domain;

import java.util.List;

public record GamesPage(
    String nextPage,
    String previousPage,
    List<Game> games
) {

}
