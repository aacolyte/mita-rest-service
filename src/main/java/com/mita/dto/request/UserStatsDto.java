package com.mita.dto.request;

public class UserStatsDto {
    private int totalItems;
    private int totalCategories;

    public UserStatsDto(int totalItems, int totalCategories) {
        this.totalItems = totalItems;
        this.totalCategories = totalCategories;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getTotalCategories() {
        return totalCategories;
    }
}
