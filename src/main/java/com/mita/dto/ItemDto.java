package com.mita.dto;


import com.mita.entity.Category;
import com.mita.entity.Item;

public class ItemDto {
    private final Long id;
    private final String title;
    private final Double rating;
    private final String additionalInfo;
    private final Long categoryId;

    public ItemDto(Long id, String title, Double rating, String additionalInfo, Long categoryId) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.additionalInfo = additionalInfo;
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
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


    }
}
