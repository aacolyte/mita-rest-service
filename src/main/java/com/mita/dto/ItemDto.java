package com.mita.dto;



public class ItemDto {
    private final Long id;
    private final String title;
    private final Double rating;
    private final String additionalInfo;
    private final Long categoryId;
    private final String poster;

    public ItemDto(Long id, String title, Double rating, String additionalInfo, Long categoryId, String poster) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.additionalInfo = additionalInfo;
        this.categoryId = categoryId;
        this.poster = poster;
    }

    public Long getId() {
        return id;
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

}

