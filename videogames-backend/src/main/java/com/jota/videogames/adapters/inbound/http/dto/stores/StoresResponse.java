package com.jota.videogames.adapters.inbound.http.dto.stores;

import com.jota.videogames.domain.Store;

import java.util.List;

public record StoresResponse(
        List<Store> stores
) {

}
