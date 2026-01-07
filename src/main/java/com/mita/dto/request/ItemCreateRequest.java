package com.mita.dto.request;

import com.mita.entity.Category;
import com.mita.entity.Item;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class ItemCreateRequest {
    private String title;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "10.0", inclusive = true)
    private Double rating;
    private String additionalInfo;
    @NotNull
    private Long categoryId;

    public ItemCreateRequest(String title, Double rating, String additionalInfo, Long categoryId) {
        this.title = title;
        this.rating = rating;
        this.additionalInfo = additionalInfo;
        this.categoryId = categoryId;
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
                category
        );
    }
}
