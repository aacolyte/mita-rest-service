package com.mita.dto.request;

import com.mita.entity.Item;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class ItemUpdateRequest {
    private final String title;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true,  message = "Rating must be between 0 and 10")
    @DecimalMax(value = "10.0", inclusive = true,  message = "Rating must be between 0 and 10")
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
