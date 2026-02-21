package com.mita.dto.request;

import com.mita.entity.Item;

public class ItemUpdateRequest {
    private final String title;


    private final Double rating;

    private final String additionalInfo;

    private final String poster;

    public ItemUpdateRequest(String title, Double rating, String additionalInfo, String poster) {
        this.title = title;
        this.rating = rating;
        this.additionalInfo = additionalInfo;
        this.poster = poster;
    }

    public String getPoster() {
        return poster;
    }

    public String getTitle() {
        return title;
    }

    public Double getRating() {
        return rating;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void applyTo(Item item) {
        item.setTitle(title);
        item.setRating(rating);
        item.setAdditionalInfo(additionalInfo);
        item.setPoster(poster);
    }
}
