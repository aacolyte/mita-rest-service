package com.mita.dto.request;

import com.mita.entity.Category;
import com.mita.entity.Item;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class ItemCreateRequest {

    private String title;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be between 0 and 10")
    @DecimalMax(value = "10.0", inclusive = true, message = "Rating must be between 0 and 10")
    private Double rating;
    private String additionalInfo;
    @NotNull
    private Long categoryId;

    private String poster;

    public ItemCreateRequest() {}

    public ItemCreateRequest(String title, Double rating, String additionalInfo, Long categoryId, String poster) {
        this.title = title;
        this.rating = rating;
        this.additionalInfo = additionalInfo;
        this.categoryId = categoryId;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public Item toEntity(Category category) {
        return new Item(
                this.getTitle(),
                this.getRating(),
                this.getAdditionalInfo(),
                category,
                this.poster
        );
    }
}
