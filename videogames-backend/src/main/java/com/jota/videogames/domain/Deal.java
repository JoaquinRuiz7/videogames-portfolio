package com.jota.videogames.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Deal {

    private Long gameId;
    private String game;
    private BigDecimal price;
    private String dealId;
    private Store store;

    public Deal(
        Long gameId,
        String game,
        String dealId,
        BigDecimal price
    ) {
        this.gameId = gameId;
        this.game = game;
        this.dealId = dealId;
        this.price = price;
    }

    public Deal(
        Long gameId,
        String game,
        String dealId,
        BigDecimal price,
        Store store
    ) {
        this.gameId = gameId;
        this.game = game;
        this.dealId = dealId;
        this.price = price;
        this.store = store;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Deal deal = (Deal) o;
        return Objects.equals(gameId, deal.gameId) && Objects.equals(game,
            deal.game) && Objects.equals(price, deal.price) && Objects.equals(
            dealId, deal.dealId) && Objects.equals(store, deal.store);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, game, price, dealId, store);
    }
}