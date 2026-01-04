package com.mita.dto;

public class ItemDto {
    private final Long id;
    private final String title;
    private final Double rating;

    public ItemDto(Long id, String title, Double rating) {
        this.id = id;
        this.title = title;
        this.rating = rating;
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
}
