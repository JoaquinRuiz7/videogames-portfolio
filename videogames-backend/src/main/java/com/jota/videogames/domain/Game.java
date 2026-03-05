package com.jota.videogames.domain;

import java.util.List;
import java.util.Objects;

public class Game {

    private Long id;
    private String name;
    private String thumbnail;
    private Integer rating;
    private Integer metaCritic;
    private ESRB esrb;
    private List<String> platforms;
    private String released;
    private List<String> screenshots;
    private GameDetails details;

    public Game(
        Long id,
        String name,
        String thumbnail,
        Integer rating,
        Integer metaCritic,
        ESRB esrb,
        List<String> platforms,
        String released,
        List<String> screenshots
    ) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.rating = rating;
        this.metaCritic = metaCritic;
        this.esrb = esrb;
        this.platforms = platforms;
        this.released = released;
        this.screenshots = screenshots;
    }

    public Game(
        Long id,
        String name,
        String thumbnail,
        Integer rating,
        Integer metaCritic,
        ESRB esrb,
        List<String> platforms,
        String released,
        List<String> screenshots,
        GameDetails details
    ) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.rating = rating;
        this.metaCritic = metaCritic;
        this.esrb = esrb;
        this.platforms = platforms;
        this.released = released;
        this.screenshots = screenshots;
        this.details = details;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getMetaCritic() {
        return metaCritic;
    }

    public void setMetaCritic(Integer metaCritic) {
        this.metaCritic = metaCritic;
    }

    public ESRB getEsrb() {
        return esrb;
    }

    public void setEsrb(ESRB esrb) {
        this.esrb = esrb;
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<String> platforms) {
        this.platforms = platforms;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public List<String> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<String> screenshots) {
        this.screenshots = screenshots;
    }

    public GameDetails getDetails() {
        return details;
    }

    public void setDetails(GameDetails details) {
        this.details = details;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Game game = (Game) o;
        return Objects.equals(id, game.id) && Objects.equals(name, game.name)
            && Objects.equals(thumbnail, game.thumbnail) && Objects.equals(rating,
            game.rating) && Objects.equals(metaCritic, game.metaCritic) && esrb == game.esrb
            && Objects.equals(platforms, game.platforms) && Objects.equals(released,
            game.released) && Objects.equals(screenshots, game.screenshots)
            && Objects.equals(details, game.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, thumbnail, rating, metaCritic, esrb, platforms, released,
            screenshots, details);
    }
}
