package com.jota.videogames.domain;

import java.util.List;

public class GameDetails {

    private String description;
    private Integer playtime;
    private String reviewUrl;
    private List<String> publishers;

    public GameDetails(
        String description,
        Integer playtime,
        String reviewUrl,
        List<String> publishers
    ) {
        this.description = description;
        this.playtime = playtime;
        this.reviewUrl = reviewUrl;
        this.publishers = publishers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPlaytime() {
        return playtime;
    }

    public void setPlaytime(Integer playtime) {
        this.playtime = playtime;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }

    public List<String> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<String> publishers) {
        this.publishers = publishers;
    }
}
