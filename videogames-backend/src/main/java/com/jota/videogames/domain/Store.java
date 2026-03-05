package com.jota.videogames.domain;

import java.util.Objects;

public class Store {

    private Long id;
    private String name;
    private String logo;

    public Store(Long id, String name, String logo) {
        this.id = id;
        this.name = name;
        this.logo = logo;
    }

    public Store(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Store store = (Store) o;
        return Objects.equals(id, store.id) && Objects.equals(name, store.name)
            && Objects.equals(logo, store.logo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, logo);
    }
}
