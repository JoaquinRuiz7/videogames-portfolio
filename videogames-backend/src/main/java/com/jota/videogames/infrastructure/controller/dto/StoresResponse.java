package com.jota.videogames.infrastructure.controller.dto;

import com.jota.videogames.domain.Store;
import java.util.List;

public record StoresResponse(
    List<Store> stores
) {

}
