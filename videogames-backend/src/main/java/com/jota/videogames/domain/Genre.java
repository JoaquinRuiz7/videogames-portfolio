package com.jota.videogames.domain;

import java.util.Objects;

public class Genre {

    private Integer id;
    private String name;
    private String image;

    public Genre(Integer id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Genre genre = (Genre) o;
        return Objects.equals(id, genre.id) && Objects.equals(name, genre.name)
            && Objects.equals(image, genre.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, image);
    }
}
