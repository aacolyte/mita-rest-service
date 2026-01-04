package com.mita.dto;

public class ItemDto {
    private final Long id;
    private final String title;
    private final Double rating;
    private final String additionalInfo;

    public ItemDto(Long id, String title, Double rating, String additionalInfo) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.additionalInfo = additionalInfo;
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
}
