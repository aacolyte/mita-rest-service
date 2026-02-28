package com.mita.dto.request.item;

import com.mita.entity.Category;
import com.mita.entity.Item;

public class ItemCreateRequest {

    private String title;

    private Double rating;

    private String additionalInfo;

    private Long categoryId;

    private String poster;


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
